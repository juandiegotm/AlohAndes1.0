package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import vos.PersonaHabilitada;
import vos.Reserva;
import vos.ReservaColectiva;

public class DAOReserva extends DAOAlohAndes{

	public DAOReserva () {
		super();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------


	public void crearReserva(Long idPersona, Long idOferta, Reserva reserva) throws SQLException, Exception {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String timestampInString = formatter.format(reserva.getFechaReserva());


		String sql = String.format("INSERT INTO %1$s.RESERVA (IDOFERTA, IDPERSONAHABILITADA, FECHARESERVA, ESTADORESERVA, IDRESERVA) VALUES (%2$d, %3$d, TO_DATE('%4$s', 'DD-MM-YYYY'), '%5$s', %6$d)",
				USUARIO,
				idOferta,
				idPersona,
				timestampInString,
				reserva.getEstadoReserva(),
				reserva.getIdReserva());

		PreparedStatement queary = conn.prepareStatement(sql);
		recursos.add(queary);
		System.out.println(sql);
		ResultSet resultado = queary.executeQuery();
	}
	
	public void crearReservaIndividualColectiva(Long idPersona, Long idOferta, Reserva reserva, Long idReservaColectiva) throws SQLException, Exception {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String timestampInString = formatter.format(reserva.getFechaReserva());


		String sql = String.format("INSERT INTO %1$s.RESERVA (IDOFERTA, IDPERSONAHABILITADA, FECHARESERVA, ESTADORESERVA, IDRESERVA, IDRESERVACOLECTIVA) VALUES (%2$d, %3$d, TO_DATE('%4$s', 'DD-MM-YYYY'), '%5$s', %6$d, %7$d)",
				USUARIO,
				idOferta,
				idPersona,
				timestampInString,
				reserva.getEstadoReserva(),
				reserva.getIdReserva(), 
				idReservaColectiva);

		PreparedStatement queary = conn.prepareStatement(sql);
		recursos.add(queary);
		System.out.println(sql);
		ResultSet resultado = queary.executeQuery();
	}

	public ObjectNode crearReservaColectiva(ReservaColectiva reservaColectiva) throws SQLException, Exception  {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode respuesta = mapper.createObjectNode();

		try {
			//Inicio de la transacción
			conn.setAutoCommit(false);
			Format formatter = new SimpleDateFormat("dd-MM-yyyy");
			String timestampInString = formatter.format(reservaColectiva.getFechaReserva());
			
			String sqlInsertReservaColectiva = String.format("INSERT INTO %1$s.RESERVACOLECTIVA (IDPERSONAHABILITADA, FECHARESERVA, ESTADORESERVA, TIPODEALOJAMIENTO, CANTIDAD, IDRESERVACOLECTIVA) VALUES ('%2$d', TO_DATE('%3$s', 'DD-MM-YYYY'), '%4$s', '%5$s', '%6$d', '%7$d')", USUARIO, reservaColectiva.getIdPersonaHabilitada(),timestampInString, 
					reservaColectiva.getEstadoReserva(), reservaColectiva.getTipoDeAlojamiento(), reservaColectiva.getCantidad(), reservaColectiva.getIdReservaColectiva());
			System.out.println("PASO 1: INTENTANDO CREAR LA RESERVA COLECTIVA EN LA BASE DE DATOS");
			System.out.println(sqlInsertReservaColectiva);

			PreparedStatement queary = conn.prepareStatement(sqlInsertReservaColectiva);
			recursos.add(queary);
			queary.executeQuery();
			
			System.out.println("EXITO: LA RESERVA COLECTIVA FUE CORRECTAMENTE AÑADIDA A LA BASE DE DATOS");
			
			//Buscar las ofertas que cumplan con las propiedades descritas
			String sqlBusquedaDeLasReservasDescritas = String.format("SELECT OFERTA.IDOFERTA, OFERTA.CANTIDADDISPONIBLE FROM %1$s.OFERTA WHERE OFERTA.FECHAINICIO >= TO_DATE('%2$s', 'DD-MM-YYYY') AND OFERTA.ESTADOOFERTA = 'habilitado' AND OFERTA.CANTIDADDISPONIBLE > 0 AND OFERTA.TIPODEOFERTA = '%3$s'",USUARIO, timestampInString, reservaColectiva.getTipoDeAlojamiento());
			System.out.println(sqlBusquedaDeLasReservasDescritas);
			
			PreparedStatement queary2 = conn.prepareStatement(sqlBusquedaDeLasReservasDescritas);
			recursos.add(queary2);
			ResultSet resultadoBusqueda = queary2.executeQuery();
			System.out.println("PASO 2: BUSCANDO OFERTA QUE CUMPLAN CON LAS CONDICIONES DESCRITAS");
			
			Integer disponibleTotal = 0;
			//Extrayendo los datos del quEAY
			ArrayNode listaOfertas = mapper.createArrayNode();
			while(resultadoBusqueda.next()) {
				ObjectNode oferta = mapper.createObjectNode();
				Integer disponible = resultadoBusqueda.getInt("CANTIDADDISPONIBLE");
				oferta.put("IDOFERTA", resultadoBusqueda.getLong("IDOFERTA"));
				oferta.put("CANTIDADDISPONIBLE", disponible);
				
				disponibleTotal += disponible;
				
				listaOfertas.add(oferta);
			}
			
			System.out.println("ATENCIÓN: VERIFICANDO QUE EXISTE LA CANTIDAD SUFICIENTE DE OFERTAS PARA CUMPLIR CON LA DEMANDA");
			
			if(disponibleTotal < reservaColectiva.getCantidad())
				throw new Exception("No existe suficiente ofertas para cumplir con la demanda");
			
			System.out.println("PASO 3: COMENZANDO EL PROCESO DE RESERVA DE LAS OFERTAS");
			Integer restante = reservaColectiva.getCantidad();
			Integer inicioLlave = 1;
			
			for(JsonNode oferta: listaOfertas) {
				JsonNode cantidadActualJSon = oferta.get("CANTIDADDISPONIBLE");
				Integer cantidadActual = cantidadActualJSon.getIntValue();
				
				JsonNode idOfertaJSon = oferta.get("IDOFERTA");
				Long idOferta = idOfertaJSon.getLongValue();
				
				
				while(cantidadActual > 0) {
					Reserva reserva = new Reserva(new Long(inicioLlave), reservaColectiva.getFechaReserva(), reservaColectiva.getEstadoReserva());
					crearReservaIndividualColectiva(reservaColectiva.getIdPersonaHabilitada(), idOferta, reserva, reservaColectiva.getIdReservaColectiva());
					cantidadActual--;
					restante--;
					inicioLlave++;
					
					if(restante == 0) {
						break;
					}
				}
				
				if(restante == 0) {
					break;
				}
				
				if(cantidadActual == 0) {
					continue;
				}
				
			}
			
			
			System.out.println("TODAS LAS RESERVAS FUERON REALIZADAS CON EXITO");
			
			System.out.println("PASO FINAL: HACIENDO COMMIT A LA BASE DE DATOS");
			conn.commit();

		}

		catch(Exception e) {
			System.out.println("Rollback");
			try {
				//deshace todos los cambios realizados en los datos
				conn.rollback();
				throw e;
			} catch (SQLException ex1) {
				System.err.println( "No se pudo deshacer" + ex1.getMessage() ); 
				
			} 


		}



		return respuesta;

	}
	
	public void cancelarReservaColectiva(Long idReservaColectiva) throws SQLException, Exception {
		
		try {
			//Inicio de la transacción 
			conn.setAutoCommit(false);
			
			System.out.println("PASO 1: BUSCAR Y ELIMAR TODAS LAS RESERVAS ASOCIADAS A LA OFERTACOLECTIVO");
			String sqlBuscarReservasConReservaColectiva = String.format("SELECT RESERVA.IDRESERVA FROM %1$s.RESERVA WHERE RESERVA.IDRESERVACOLECTIVA = %2$d", USUARIO, idReservaColectiva);
			PreparedStatement queary1 = conn.prepareStatement(sqlBuscarReservasConReservaColectiva);
			ResultSet result = queary1.executeQuery();
			
			while(result.next()) {
				Long idReserva = result.getLong("IDRESERVA");
				System.out.println("ATENCION: ELIMINANDO LA RESERVA CON ID: " + idReserva);
				deleteReserva(idReserva);
			}
			
			System.out.println("PASO 2: ELIMINAR LA RESERVA COLECTIVA");
			String sqlEliminarReservaColectiva = String.format("DELETE FROM %1$s.RESERVACOLECTIVA WHERE IDRESERVACOLECTIVA = %2$d", USUARIO, idReservaColectiva);
			
			PreparedStatement queary2 = conn.prepareStatement(sqlEliminarReservaColectiva);
			queary2.executeQuery();
			
			System.out.println("PROCESO DE ELIMINACION EXITOSO");
			
			System.out.println("PASO FINAL: HACIENDO COMMIT A LA BASE DE DATOS");
			
			
			conn.commit();
		}
		
		catch(Exception e){
			
			System.out.println("Rollback");
			try {
				//deshace todos los cambios realizados en los datos
				conn.rollback();
				throw e;
			} catch (SQLException ex1) {
				System.err.println( "No se pudo deshacer" + ex1.getMessage() ); 
				
			} 
		}
		
		
		
		
	}

	public void deleteReserva(Long idReserva) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.RESERVA WHERE IDRESERVA = %2$d", USUARIO, idReserva);

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public List<Reserva> reservasDeOferta(Long idOferta) throws SQLException, Exception {
		List<Reserva> reservas = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.RESERVA WHERE IDOFERTA = %2$d", USUARIO, idOferta);
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();


		if(resultado.next()) {
			reservas.add(convetirResultSet(resultado));
		}

		return reservas;

	}

	//Este metodo lo que hace es unificar los tres metodos anteriores en una lista de listas
	/**
	 * RFC7 - Para una unidad de tiempo definido (por ejemplo, semana o mes) y un tipo de alojamiento, 
	 * considerando todo el tiempo de operación de AloHandes, indicar cuáles fueron las fechas de mayor 
	 * demanda (mayor cantidad de alojamientos ocupados), las de mayores ingresos (mayor cantidad de 
	 * dinero recibido) y las de menor ocupación.
	 * @param periodoParam
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getOperacionDeAlohAndes(String periodoParam, String tipoOferta) throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode periodosYValoresTotales = mapper.createArrayNode();

		ObjectNode periodoConMasReservas = mapper.createObjectNode();
		List<Integer>periodoConMasReservasInteger = getPeriodoConMasReservas(periodoParam, tipoOferta);
		periodoConMasReservas.put("PeriodoConMasReservas", periodoConMasReservasInteger.get(0));
		periodoConMasReservas.put("Cantidad", periodoConMasReservasInteger.get(1));

		periodosYValoresTotales.add(periodoConMasReservas);

		ObjectNode periodoConMasIngresos = mapper.createObjectNode();
		List<Integer>periodoConMasIngresosInteger = getPeriodoConMasIngresos(periodoParam, tipoOferta);
		periodoConMasIngresos.put("PeriodoConMasIngresos", periodoConMasIngresosInteger.get(0));
		periodoConMasIngresos.put("valorTotal", periodoConMasIngresosInteger.get(1));

		periodosYValoresTotales.add(periodoConMasIngresos);

		ObjectNode periodoConMenosReservas = mapper.createObjectNode();
		List<Integer> periodoConMenosReservasInteger = getPeriodoConMenosReservas(periodoParam, tipoOferta);
		periodoConMenosReservas.put("PeriodoConMenosReservas", periodoConMenosReservasInteger.get(0));
		periodoConMenosReservas.put("Cantidad", periodoConMenosReservasInteger.get(1));

		periodosYValoresTotales.add(periodoConMenosReservas);

		return periodosYValoresTotales;
	}

	private List<Integer> getPeriodoConMasReservas(String periodoParam, String tipoOferta) throws SQLException, Exception {
		List<Integer> periodoYcantidad = new ArrayList<>();

		String sql = String.format("SELECT PERIODO_CON_MAS_RESERVAS, CANTIDAD_RESERVAS FROM( "
				+"SELECT PERIODO AS PERIODO_CON_MAS_RESERVAS, COUNT (PERIODO) AS CANTIDAD_RESERVAS, ROW_NUMBER() OVER (ORDER BY  COUNT (PERIODO) DESC)AS POSICION " 
				+"FROM "
				+"(SELECT IDRESERVA, EXTRACT( %2$s FROM FECHARESERVA) PERIODO FROM %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA WHERE OFERTA.TIPODEOFERTA='%3$s') " 
				+"GROUP BY PERIODO) "
				+"WHERE POSICION=1", USUARIO, periodoParam, tipoOferta);
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();
		resultado.next();

		periodoYcantidad.add(resultado.getInt("PERIODO_CON_MAS_RESERVAS"));
		periodoYcantidad.add(resultado.getInt("CANTIDAD_RESERVAS"));

		return periodoYcantidad;
	}

	private List<Integer> getPeriodoConMasIngresos(String periodoParam, String tipoOferta) throws SQLException, Exception {
		List<Integer> periodoYvalor = new ArrayList<>();

		String sql = String.format("SELECT PERIODO_CON_MAS_INGRESOS, VALOR_TOTAL FROM ( "
				+"SELECT PERIODO AS PERIODO_CON_MAS_INGRESOS, VALOR_TOTAL, ROW_NUMBER() OVER (ORDER BY VALOR_TOTAL DESC)AS POSICION " 
				+"FROM "
				+"(SELECT PERIODO, SUM (VALOR_TOTAL_RESERVA) AS VALOR_TOTAL FROM "
				+"(SELECT RESERVA.IDRESERVA, OFERTA.VALOR*OFERTA.DURACION AS VALOR_TOTAL_RESERVA, EXTRACT( %2$s FROM RESERVA.FECHARESERVA)PERIODO FROM ( %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) " 
				+"WHERE OFERTA.TIPODEOFERTA='%3$s') "
				+"GROUP BY PERIODO) "
				+")WHERE POSICION=1", USUARIO, periodoParam, tipoOferta);
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();
		resultado.next();

		periodoYvalor.add(resultado.getInt("PERIODO_CON_MAS_INGRESOS"));
		periodoYvalor.add(resultado.getInt("VALOR_TOTAL"));

		return periodoYvalor;
	}

	private List<Integer> getPeriodoConMenosReservas(String periodoParam, String tipoOferta) throws SQLException, Exception {
		List<Integer> periodoYcantidad = new ArrayList<>();

		String sql = String.format("SELECT PERIODO_CON_MENOS_RESERVAS, CANTIDAD_RESERVAS FROM( "
				+"SELECT PERIODO AS PERIODO_CON_MENOS_RESERVAS, COUNT (PERIODO) AS CANTIDAD_RESERVAS, ROW_NUMBER() OVER (ORDER BY  COUNT (PERIODO) ASC)AS POSICION " 
				+"FROM "
				+"(SELECT IDRESERVA, EXTRACT( %2$s FROM FECHARESERVA)PERIODO FROM %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA WHERE OFERTA.TIPODEOFERTA='%3$s') " 
				+"GROUP BY PERIODO) "
				+"WHERE POSICION=1", USUARIO, periodoParam, tipoOferta);
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();
		resultado.next();

		periodoYcantidad.add(resultado.getInt("PERIODO_CON_MENOS_RESERVAS"));
		periodoYcantidad.add(resultado.getInt("CANTIDAD_RESERVAS"));

		return periodoYcantidad;
	}



	public Reserva convetirResultSet(ResultSet resultado) throws SQLException {

		Long idReserva = resultado.getLong("IDRESERVA");
		String estado = resultado.getString("ESTADORESERVA");
		Date fechaReserva = new Date(resultado.getTimestamp("FECHARESERVA").getDate());

		Reserva reserva = new Reserva(idReserva, fechaReserva, estado);

		return reserva;

	}



}

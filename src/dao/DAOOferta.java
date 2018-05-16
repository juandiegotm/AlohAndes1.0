package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import vos.Oferta;
import vos.PersonaHabilitada;

public class DAOOferta extends DAOAlohAndes {


	/**
	 * Retorna todas las ofertas (tanto habilitadas como no) de AlohAndes
	 * @return todas las ofertas de AlohAndes
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<Oferta> getOfertas() throws SQLException, Exception{
		ArrayList<Oferta> oferta = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.OFERTA", USUARIO);

		PreparedStatement query = conn.prepareStatement(sql);
		ResultSet resultado = query.executeQuery();

		recursos.add(query);

		while(resultado.next()){
			oferta.add(convetirResultSet(resultado));
		}


		return oferta;

	}

	/**
	 * Busca una oferta de alojamiento por ID. Retorna la oferta si la encuetra, null de lo contrario.
	 * @param id de la oferta que se est� buscando.
	 * @return la oferta si la encuentra. Null de lo contrario. 
	 * @throws SQLException
	 * @throws Exception
	 */
	public Oferta buscarOfertaPorId(Long id) throws SQLException, Exception {
		Oferta ofertaBuscada = null;

		String sql = String.format("SELECT * FROM %1$s.OFERTA WHERE IDOFERTA='%2$d'", USUARIO, id);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();


		if(resultado.next()) {
			ofertaBuscada = convetirResultSet(resultado);
		}
		return ofertaBuscada;
	}

	/**
	 * RF6 - Retira una oferta de alojamiento permanentemente. 
	 * @param idOferta que se quiere retirar permanentemente. 
	 * @throws SQLException
	 * @throws Exception
	 */
	public void retirarOferta(Long idOferta) throws SQLException, Exception  {

		String sql = String.format("DELETE FROM %1$s.OFERTA WHERE IDOFERTA = %2$d", USUARIO, idOferta);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * RFC2 - Muestra las 20 ofertas m�s populares
	 * @return las 20 ofertas m�s populares
	 * @throws SQLException Si existe un error en la sentencia SQL
	 * @throws Exception Si ocurre cualquer otra excepci�n. 
	 */
	public ArrayNode getOfertasMasPopulares() throws SQLException, Exception{
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ofertas = mapper.createArrayNode();

		String sql = "SELECT TOP_POPULA, IDOFERTA, TIPODEOFERTA, DISPONIBLE, VALOR, FECHA_INICIO, FECHA_FINAL "
				+ "FROM( "
				+ "SELECT OFERTA.IDOFERTA AS IDOFERTA, OFERTA.TIPODEOFERTA AS TIPODEOFERTA, OFERTA.CANTIDADDISPONIBLE AS DISPONIBLE, OFERTA.VALOR AS VALOR,OFERTA.FECHAINICIO AS FECHA_INICIO,OFERTA.FECHAFINAL AS FECHA_FINAL, COUNT(OFERTA.IDOFERTA) AS RESERVAS ,ROW_NUMBER() OVER (ORDER BY  COUNT (OFERTA.IDOFERTA) DESC)AS TOP_POPULA "
				+ String.format("FROM  %1$s.OFERTA INNER JOIN %1$s.RESERVA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA AND OFERTA.ESTADOOFERTA='habilitado' ", USUARIO)
				+ "GROUP BY OFERTA.IDOFERTA, OFERTA.TIPODEOFERTA, OFERTA.CANTIDADDISPONIBLE, OFERTA.VALOR, OFERTA.FECHAINICIO,OFERTA.FECHAFINAL "
				+ "ORDER BY OFERTA.IDOFERTA) "
				+ "WHERE TOP_POPULA<20 ORDER BY TOP_POPULA";

		PreparedStatement query = conn.prepareStatement(sql);


		ResultSet resultado = query.executeQuery();

		recursos.add(query);

		while(resultado.next()){
			ObjectNode ofertaActual = mapper.createObjectNode();
			Integer topPopula = resultado.getInt("TOP_POPULA");
			Long idOferta =  resultado.getLong("IDOFERTA");
			String tipoDeOferta = resultado.getString("TIPODEOFERTA");
			Integer cantidad = resultado.getInt("DISPONIBLE");
			Timestamp fechaInicio = resultado.getTimestamp("FECHA_INICIO");
			Timestamp fechaFinal = resultado.getTimestamp("FECHA_FINAL");
			Double valor = resultado.getDouble("VALOR");

			ofertaActual.put("Posicion", topPopula);
			ofertaActual.put("Id", idOferta);
			ofertaActual.put("Tipo", tipoDeOferta);
			ofertaActual.put("Cantidad", cantidad);
			ofertaActual.put("fechaInicio", fechaInicio.toString());
			ofertaActual.put("fechaFinal", fechaFinal.toString());
			ofertaActual.put("Valor", valor);

			ofertas.add(ofertaActual);


		}


		return ofertas;

	}

	/**
	 * RFC3 - Mostrar el indice de ocupaci�n de cada una de las ofertas de alojamiento registradas. 
	 * @return el indice de ocupaci�n de todas las ofertas habilitadas de alohAndes. 
	 * @throws Exception
	 * @throws SQLException
	 */
	public ArrayNode getIndiceOcupacionPorOferta() throws Exception, SQLException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ofertas = mapper.createArrayNode();

		String sql = String.format("SELECT OFERTA.IDOFERTA, 100-(100/OFERTA.CANTIDADINICIAL)*OFERTA.CANTIDADDISPONIBLE AS PORCENTAJE_OCUPACION\r\n" + 
				"FROM %1$s.OFERTA WHERE ESTADOOFERTA='habilitado' ORDER BY PORCENTAJE_OCUPACION DESC", USUARIO);

		PreparedStatement query = conn.prepareStatement(sql);

		ResultSet resultado = query.executeQuery();

		recursos.add(query);

		while(resultado.next()) {
			ObjectNode oferta = mapper.createObjectNode();

			Long id = resultado.getLong("IDOFERTA");
			Double porcentajeOcupacion = resultado.getDouble("PORCENTAJE_OCUPACION");

			oferta.put("IdOferta", id);
			oferta.put("PorcentajeOcupacion", porcentajeOcupacion);

			ofertas.add(oferta);
		}


		return ofertas;

	}



	/**
	 * RFC4 - Mostrar los alojamientos disponibles en un rango de fechas, que cumplen con un conjunto de 
	 * requerimientos de dotaci�n o servicios, por ejemplo, cocineta, TV Cable, Internet, sala
	 * @param fechaInicio anterior a la fecha de inicio de los alojamientos buscados. 
	 * @param fechaFinal despues de la fecha de terminaci�n de los alojaminetos buscados. 
	 * @param servicios con los que se quiere que cuente los alojaminetos buscado. 
	 * @return Lista de alojamientos buscados que cumplen con las condiciones de fechas y servicios. 
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getOfertasEnRangoDeFechasYCiertosServicios(Date fechaInicio, Date fechaFinal, List<String> servicios) throws SQLException, Exception{

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ofertas = mapper.createArrayNode();

		String sql=null;
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");

		String timestampFechaInicio = formatter.format(fechaInicio);
		String timestampFechaFinal = formatter.format(fechaFinal);


//		StringBuilder x = new  StringBuilder();
//		x.append(str)

		sql = String.format("SELECT OFERTA.IDOFERTA, OFERTA.FECHAINICIO, OFERTA.FECHAFINAL, COUNT(*) "
				+"FROM %1$s.OFERTA INNER JOIN %1$s.SERVICIO ON SERVICIO.IDOFERTA = OFERTA.IDOFERTA "
				+"WHERE OFERTA.FECHAINICIO >= TO_DATE('%2$s', 'DD-MM-YYYY') AND OFERTA.FECHAFINAL<= TO_DATE('%3$s', 'DD-MM-YYYY')"
				+"AND OFERTA.CANTIDADDISPONIBLE>0 AND OFERTA.ESTADOOFERTA='habilitado' 	", USUARIO, timestampFechaInicio, timestampFechaFinal);

		for (int i = 0; i < servicios.size(); i++) 
			if(i == 0) 
				sql = sql + String.format("AND SERVICIO.NOMBRESERVICIO= '%1$s' ", servicios.get(i));	
			else 
				sql = sql + String.format("OR SERVICIO.NOMBRESERVICIO= '%1$s' ", servicios.get(i));



		sql += String.format("GROUP BY OFERTA.IDOFERTA, OFERTA.FECHAINICIO, OFERTA.FECHAFINAL HAVING COUNT(*) = %1$d", servicios.size());

		System.out.println(sql);

		PreparedStatement query = conn.prepareStatement(sql);
		ResultSet resultado = query.executeQuery();

		recursos.add(query);


		while(resultado.next()){
			ObjectNode oferta = mapper.createObjectNode();
			ArrayNode serviciosRespuesta = mapper.valueToTree(servicios);

			Long id = resultado.getLong("IDOFERTA");
			Date fechaInicioRespuesta = resultado.getTimestamp("FECHAINICIO");
			Date fechaFinRespuesta = resultado.getTimestamp("FECHAFINAL");

			oferta.put("Id", id);
			oferta.put("fechaInicio", fechaInicioRespuesta.toString());
			oferta.put("fechaFin", fechaFinRespuesta.toString());
			oferta.putArray("servicios").addAll(serviciosRespuesta);

			ofertas.add(oferta);
		}

		return ofertas;
	}


	/**
	 * RFC9 - Encontrar las ofertas de alojamiento que no tienen mucha demanda.  Encontrar las ofertas de 
	 * alojamiento que no han recibido clientes en periodos superiores a 1 mes, durante todo el periodo 
	 * de operaci�n de AlohAndes.
	 * @return ofertas de alojamiento que no tiene mucha demanda. 
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<Oferta> getOfertasConPocaDemanda() throws SQLException, Exception{
		ArrayList<Oferta> ofertasConPocaDemanda = new ArrayList<>();

		String sql = String.format("SELECT * FROM ( ((SELECT OFERTA.IDOFERTA AS ID_OFERTA FROM %1$s.OFERTA) MINUS (SELECT OFERTA.IDOFERTA FROM (%1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) WHERE RESERVA.FECHARESERVA <= OFERTA.FECHAPUBLICACION+30)) UNION (SELECT R1.IDOFERTA AS ID_OFERTA FROM (%1$s.RESERVA R1 INNER JOIN %1$s.RESERVA R2 ON R1.IDOFERTA = R2.IDOFERTA AND R1.FECHARESERVA < R2.FECHARESERVA) WHERE (R1.FECHARESERVA + 30 <= R2.FECHARESERVA)) UNION (SELECT ID_OFERTA FROM ( SELECT OFERTA.IDOFERTA AS ID_OFERTA, MAX(RESERVA.FECHARESERVA) FECHA_ULTIMA_RESERVA FROM (%1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) GROUP BY OFERTA.IDOFERTA) WHERE FECHA_ULTIMA_RESERVA+30 <= CURRENT_TIMESTAMP) ) INNER JOIN %1$s.OFERTA ON OFERTA.IDOFERTA=ID_OFERTA", USUARIO);

		PreparedStatement query = conn.prepareStatement(sql);
		System.out.println(sql);
		ResultSet resultado = query.executeQuery();

		recursos.add(query);

		while(resultado.next()){
			ofertasConPocaDemanda.add(convetirResultSet(resultado));
		}

		return ofertasConPocaDemanda;

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PARA EL RF9
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void deshabilitarOferta(long idOferta) throws SQLException, Exception{
		String sql = String.format("UPDATE OFERTA SET ESTADOOFERTA = 'deshabilitado' WHERE IDOFERTA="+idOferta);

		ArrayList<Long> reservasParaRecorrer= obtenerReservasParaDeshabilitar(idOferta);

		if(reservasParaRecorrer!=null){
			DAOReserva daoReserva= new DAOReserva();
			for (int i = 0; i < reservasParaRecorrer.size(); i++) {

				daoReserva.deleteReserva(reservasParaRecorrer.get(i));

			}
		}

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public ArrayList<Long> obtenerReservasParaDeshabilitar(long idOferta) throws SQLException, Exception
	{
		ArrayList<Long> reservasParaDeshabilitar = new ArrayList<>();


		String sql = String.format("SELECT RESERVA.IDRESERVA FROM RESERVA"
				+"WHERE RESERVA.IDOFERTA="+idOferta
				+"AND ESTADORESERVA!='finalizado'");

		System.out.println(sql);

		PreparedStatement query = conn.prepareStatement(sql);
		ResultSet resultado = query.executeQuery();
		recursos.add(query);
		while(resultado.next()){
			reservasParaDeshabilitar.add(convetirResultSetVersion2(resultado));
		}
		return reservasParaDeshabilitar;
	}

	public long convetirResultSetVersion2(ResultSet resultado) throws SQLException {

		Long idReserva = resultado.getLong("IDRESERVA");

		return idReserva;

	}


	public void habilitarOferta(long idOferta) throws SQLException, Exception{
		String sql = String.format("UPDATE OFERTA SET ESTADOOFERTA = 'habilitado' WHERE IDOFERTA="+idOferta+ ";", USUARIO, idOferta);

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public Oferta convetirResultSet(ResultSet resultado) throws SQLException {



		Long idOferta = resultado.getLong("IDOFERTA");
		Double valor = resultado.getDouble("VALOR");
		String duracion = resultado.getString("DURACION");
		Timestamp fechaInicio = resultado.getTimestamp("FECHAINICIO");
		Timestamp fechaFinal = resultado.getTimestamp("FECHAFINAL");
		Integer cantidadDisponible = resultado.getInt("CANTIDADDISPONIBLE");
		String direccion = resultado.getString("DIRECCION");
		String tipoOferta = resultado.getString("TIPODEOFERTA");
		Integer cantidadInicial = resultado.getInt("CANTIDADINICIAL");



		Oferta oferta = new Oferta(idOferta, valor, duracion, fechaInicio, fechaFinal, cantidadDisponible, direccion, tipoOferta, cantidadInicial);

		return oferta;
	}



}

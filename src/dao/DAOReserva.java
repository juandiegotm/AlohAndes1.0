package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vos.PersonaHabilitada;
import vos.Reserva;

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
	
	
	public Reserva convetirResultSet(ResultSet resultado) throws SQLException {

		Long idReserva = resultado.getLong("IDRESERVA");
		String estado = resultado.getString("ESTADORESERVA");
		Date fechaReserva = new Date(resultado.getTimestamp("FECHARESERVA").getDate());
		
		Reserva reserva = new Reserva(idReserva, fechaReserva, estado);
		
		return reserva;

	}
	
	public List<Integer> getPeriodoConMasReservas(String periodoParam) throws SQLException, Exception {
		List<Integer> periodoYcantidad = new ArrayList<>();
		
		String sql = String.format("SELECT PERIODO_CON_MAS_RESERVAS, CANTIDAD_RESERVAS FROM("
									+"SELECT PERIODO AS PERIODO_CON_MAS_RESERVAS, COUNT (PERIODO) AS CANTIDAD_RESERVAS, ROW_NUMBER() OVER (ORDER BY  COUNT (PERIODO) DESC)AS POSICION" 
										+"FROM"
											+"(SELECT IDRESERVA, EXTRACT( "+periodoParam+ " FROM FECHARESERVA)PERIODO FROM RESERVA INNER JOIN OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA WHERE OFERTA.TIPODEOFERTA='OFERTAHOTEL')" 
										+"GROUP BY PERIODO)"
									+"WHERE POSICION=1;");
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();
		
		periodoYcantidad.set(0, resultado.getInt("PERIODO_CON_MAS_RESERVAS"));
		periodoYcantidad.set(1, resultado.getInt("CANTIDAD_RESERVAS"));
		
		return periodoYcantidad;
	}
	
	public List<Integer> getPeriodoConMasIngresos(String periodoParam) throws SQLException, Exception {
		List<Integer> periodoYvalor = new ArrayList<>();
		
		String sql = String.format("SELECT PERIODO_CON_MAS_INGRESOS, VALOR_TOTAL FROM ("
									    +"SELECT PERIODO AS PERIODO_CON_MAS_INGRESOS, VALOR_TOTAL, ROW_NUMBER() OVER (ORDER BY VALOR_TOTAL DESC)AS POSICION" 
									        +"FROM"
									            +"(SELECT PERIODO, SUM (VALOR_TOTAL_RESERVA) AS VALOR_TOTAL FROM"
									            +"(SELECT RESERVA.IDRESERVA, OFERTA.VALOR*OFERTA.DURACION AS VALOR_TOTAL_RESERVA, EXTRACT( "+periodoParam+ " FROM RESERVA.FECHARESERVA)PERIODO FROM (RESERVA INNER JOIN OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA)" 
									            +"WHERE OFERTA.TIPODEOFERTA='OFERTAHOTEL')"
									    +"GROUP BY PERIODO)"
									+")WHERE POSICION=1;");
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();
		
		periodoYvalor.set(0, resultado.getInt("PERIODO_CON_MAS_INGRESOS"));
		periodoYvalor.set(1, resultado.getInt("VALOR_TOTAL"));
		
		return periodoYvalor;
	}
	
	public List<Integer> getPeriodoConMenosReservas(String periodoParam) throws SQLException, Exception {
		List<Integer> periodoYcantidad = new ArrayList<>();
		
		String sql = String.format("SELECT PERIODO_CON_MENOS_RESERVAS, CANTIDAD_RESERVAS FROM("
								    +"SELECT PERIODO AS PERIODO_CON_MENOS_RESERVAS, COUNT (PERIODO) AS CANTIDAD_RESERVAS, ROW_NUMBER() OVER (ORDER BY  COUNT (PERIODO) ASC)AS POSICION" 
								        +"FROM"
								            +"(SELECT IDRESERVA, EXTRACT( "+periodoParam+ " FROM FECHARESERVA)PERIODO FROM RESERVA INNER JOIN OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA WHERE OFERTA.TIPODEOFERTA='OFERTAHOTEL')" 
								        +"GROUP BY PERIODO)"
								    +"WHERE POSICION=1;");
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();
		
		periodoYcantidad.set(0, resultado.getInt("PERIODO_CON_MENOS_RESERVAS"));
		periodoYcantidad.set(1, resultado.getInt("CANTIDAD_RESERVAS"));
		
		return periodoYcantidad;
	}
	
	//Este metodo lo que hace es unificar los tres metodos anteriores en una lista de listas
	
	public List<List<Integer>> getOperacionDeAlohAndes(String periodoParam) throws SQLException, Exception {
		List<List<Integer>> periodosYValoresTotales = new ArrayList<>();
		
		periodosYValoresTotales.add(getPeriodoConMasReservas(periodoParam));
		periodosYValoresTotales.add(getPeriodoConMasIngresos(periodoParam));
		periodosYValoresTotales.add(getPeriodoConMenosReservas(periodoParam));
		
		return periodosYValoresTotales;
	}


}

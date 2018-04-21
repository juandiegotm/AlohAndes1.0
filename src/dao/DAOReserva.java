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


}

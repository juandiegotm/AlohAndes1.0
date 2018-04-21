package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.PersonaHabilitada;

public class DAOPersonaHabilitada extends DAOAlohAndes {

	public DAOPersonaHabilitada() {
		super();
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	public ArrayList<PersonaHabilitada> getPersonasHabiltadas() throws SQLException, Exception{
		ArrayList<PersonaHabilitada> personas = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.PERSONAHABILITADA", USUARIO);

		PreparedStatement query = conn.prepareStatement(sql);
		ResultSet resultado = query.executeQuery();

		recursos.add(query);

		while(resultado.next()){
			personas.add(convetirResultSet(resultado));
		}


		return personas;

	}

	public void agregarPersonaHabilitada(PersonaHabilitada persona) throws SQLException, Exception {

		
		String sql = String.format("INSERT INTO %1$s.PERSONAHABILITADA (IDPERSONAHABILITADA, NOMBRE, TELEFONO, EMAIL) VALUES (%2$d, '%3$s', %4$d, '%5$s')", 
				USUARIO, 
				persona.getIdPersonaHabilitada(), 
				persona.getNombre(),	
				persona.getTelefono(),
				persona.getEmail());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();



	}

	public PersonaHabilitada buscarPersonaHabilitadaPorId(Long id) throws SQLException, Exception {
		PersonaHabilitada personaBuscada = null;

		String sql = String.format("SELECT * FROM %1$s.PERSONAHABILITADA WHERE IDPERSONAHABILITADA='%2$d'", USUARIO, id);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();


		if(resultado.next()) {
			personaBuscada = convetirResultSet(resultado);
		}
		return personaBuscada;
	}


	public PersonaHabilitada convetirResultSet(ResultSet resultado) throws SQLException {

		Long id = resultado.getLong("idpersonahabilitada");
		String nombre = resultado.getString("nombre");
		Long cedula = id;
		Long telefono = resultado.getLong("telefono");
		String correo = resultado.getString("email");



		PersonaHabilitada persona = new PersonaHabilitada(id,nombre,cedula,telefono,correo);
		return persona;


	}


}

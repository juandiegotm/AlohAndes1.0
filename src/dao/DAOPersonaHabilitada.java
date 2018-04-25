package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

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

	public List<PersonaHabilitada> getClientesFrecuentes(Long idOferta) throws SQLException, Exception {
		List<PersonaHabilitada> clientesFrecuentes = new ArrayList<>();

		String sql = String.format("(SELECT IDPERSONAHABILITADA, NOMBRE, TELEFONO, EMAIL " 
				+"FROM %1$s.PERSONAHABILITADA INNER JOIN "
				+"(SELECT  RESERVA.IDPERSONAHABILITADA AS ID "
				+"FROM (%1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) WHERE OFERTA.DURACION>=15 AND OFERTA.IDOFERTA = %2$d) " 
				+"ON PERSONAHABILITADA.IDPERSONAHABILITADA = ID) "

									+"UNION"

									    +"(SELECT IDPERSONAHABILITADA, NOMBRE, TELEFONO, EMAIL "
									    +"FROM %1$s.PERSONAHABILITADA INNER JOIN( "
									    +"SELECT IDPERSON FROM ( "
									    +"SELECT IDPERSON, JUNTITOS, COUNT (JUNTITOS) AS OCASIONES FROM ( "
									    +"SELECT IDPERSON, CONCAT (IDPERSON, IDOFER) AS JUNTITOS FROM( "
									    + "SELECT RESERVA.IDPERSONAHABILITADA AS IDPERSON, OFERTA.IDOFERTA AS IDOFER "
									    +"FROM (%1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA AND OFERTA.IDOFERTA =  %2$d))) " 
									    +"GROUP BY JUNTITOS, IDPERSON) "
									    +"WHERE OCASIONES>=2) "
									    +"ON PERSONAHABILITADA.IDPERSONAHABILITADA = IDPERSON "
									    +")", USUARIO, idOferta);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet resultado = prepStmt.executeQuery();

		if(resultado.next()) {
			clientesFrecuentes.add(convetirResultSet(resultado));
		}
		return clientesFrecuentes;
	}


	public ArrayNode getUsoDeAlohAndesPorCadaUsuario() throws SQLException, Exception{
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode usuarios = mapper.createArrayNode();

		String sql = String.format("SELECT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, OFERTA.TIPODEOFERTA AS CARACTERISTICA, OFERTA.DURACION AS DIAS_CONTRATADOS, OFERTA.VALOR AS VALOR_DIA FROM (( %1$s.PERSONAHABILITADA INNER JOIN %1$s.RESERVA ON RESERVA.IDPERSONAHABILITADA = PERSONAHABILITADA.IDPERSONAHABILITADA) INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) WHERE RESERVA.ESTADORESERVA='finalizado'", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();


		while(resultado.next()) {
			ObjectNode usuario = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado.getLong("IDPERSONAHABILITADA");
			String nombre = resultado.getString("NOMBRE");
			String caracteristica = resultado.getString("CARACTERISTICA");
			Integer diasContratados = resultado.getInt("DIAS_CONTRATADOS");
			Double valorDia = resultado.getDouble("VALOR_DIA");

			usuario.put("Id", idPersonasHabilitada);
			usuario.put("Nombre:", nombre);
			usuario.put("Caracteristuca", caracteristica);
			usuario.put("DiasContratados", diasContratados);
			usuario.put("ValorDia", valorDia);

			usuarios.add(usuario);
		}

		return usuarios;

	}
	
	/**
	 * RFC6 - Mostrar el uso de AlohAndes para un usuario dado.
	 * @param id del usuario de AlohAndes que se quiere consultar
	 * @return el uso de AlohAndes para el usuario con el id dado por parametro
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getUsoDeAlohAndesPorUsuarioEspecifico(Long id) throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode usuarios = mapper.createArrayNode();

		String sql = String.format("SELECT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, OFERTA.TIPODEOFERTA AS CARACTERISTICA, OFERTA.DURACION AS DIAS_CONTRATADOS, OFERTA.VALOR AS VALOR_DIA FROM (( %1$s.PERSONAHABILITADA INNER JOIN %1$s.RESERVA ON RESERVA.IDPERSONAHABILITADA = PERSONAHABILITADA.IDPERSONAHABILITADA) INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) WHERE RESERVA.ESTADORESERVA='finalizado' AND PERSONAHABILITADA.IDPERSONAHABILITADA = %2$d", USUARIO, id);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();

		while(resultado.next()){
			ObjectNode usuario = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado.getLong("IDPERSONAHABILITADA");
			String nombre = resultado.getString("NOMBRE");
			String caracteristica = resultado.getString("CARACTERISTICA");
			Integer diasContratados = resultado.getInt("DIAS_CONTRATADOS");
			Double valorDia = resultado.getDouble("VALOR_DIA");

			usuario.put("Id", idPersonasHabilitada);
			usuario.put("Nombre:", nombre);
			usuario.put("Caracteristuca", caracteristica);
			usuario.put("DiasContratados", diasContratados);
			usuario.put("ValorDia", valorDia);

			usuarios.add(usuario);
		}

		return usuarios;


	}


}

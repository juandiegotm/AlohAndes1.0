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
			usuario.put("Caracteristica", caracteristica);
			usuario.put("DiasContratados", diasContratados);
			usuario.put("ValorDia", valorDia);

			usuarios.add(usuario);
		}

		return usuarios;
	}


	/**
	 * RFC10_PARTE1 - Mostrar los usuarios (persona habilitada) que han hecho reservas en el rango de fechas indicado
	 * con un tipo de oferta definido y ofernado por un tipo de dato del cliente
	 * @param rango de fechas CON FORMATO '01/03/18', tipo de oferta, tipo de dato de cliente para ordenar
	 * @return los usuarios que cumplen con los criterios
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getConsumoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(String fechaInicio, String fechaFinal, String tipoDeOferta, String datoOrdenar) throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode personasHabilitadas = mapper.createArrayNode();

		String sql = 
				String.format("SELECT DISTINCT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, PERSONAHABILITADA.EMAIL, PERSONAHABILITADA.TELEFONO\r\n" + 
						"FROM\r\n" + 
						"((%1$s.PERSONAHABILITADA INNER JOIN %1$s.RESERVA ON PERSONAHABILITADA.IDPERSONAHABILITADA = RESERVA.IDPERSONAHABILITADA) \r\n" + 
						"    INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA )\r\n" + 
						"WHERE OFERTA.TIPODEOFERTA = %4$s \r\n" + 
						"AND RESERVA.FECHARESERVA>= %2$s AND RESERVA.FECHARESERVA<= %3$s \r\n" + 
						"ORDER BY PERSONAHABILITADA.%5$s);", USUARIO, fechaInicio, fechaFinal, tipoDeOferta, datoOrdenar);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();

		while(resultado.next()){
			ObjectNode personaHabilitada = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado.getLong("IDPERSONAHABILITADA");
			String nombre = resultado.getString("NOMBRE");
			String email = resultado.getString("EMAIL");
			Long telefono = resultado.getLong("TELEFONO");

			personaHabilitada.put("Id", idPersonasHabilitada);
			personaHabilitada.put("Nombre:", nombre);
			personaHabilitada.put("Email", email);
			personaHabilitada.put("Telefono", telefono);

			personasHabilitadas.add(personaHabilitada);
		}

		return personasHabilitadas;
	}



	/**
	 * RFC10_PARTE2 - Mostrar los usuarios (persona habilitada) que han hecho reservas en el rango de fechas indicado y agrupado por el tipo de oferta
	 * @param rango de fechas CON FORMATO '01/03/18'
	 * @return los usuarios que cumplen con los criterios
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getConsumoDeAlohAndesDadoRangoFechasAgrupadoTipoOferta(String fechaInicio, String fechaFinal) throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode personasHabilitadas = mapper.createArrayNode();

		String sql = 
				String.format("SELECT DISTINCT OFERTA.TIPODEOFERTA, PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, \r\n" + 
						"PERSONAHABILITADA.EMAIL, PERSONAHABILITADA.TELEFONO\r\n" + 
						"FROM\r\n" + 
						"((%1$s.PERSONAHABILITADA INNER JOIN %1$s.RESERVA ON PERSONAHABILITADA.IDPERSONAHABILITADA = \r\n" + 
						"RESERVA.IDPERSONAHABILITADA) \r\n" + 
						"    INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA )\r\n" + 
						"WHERE RESERVA.FECHARESERVA>='01/03/18' AND RESERVA.FECHARESERVA<='30/12/18'\r\n" + 
						"GROUP BY OFERTA.TIPODEOFERTA,PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, \r\n" + 
						"PERSONAHABILITADA.EMAIL, PERSONAHABILITADA.TELEFONO \r\n" + 
						"ORDER BY OFERTA.TIPODEOFERTA;", USUARIO, fechaInicio, fechaFinal);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();

		while(resultado.next()){
			ObjectNode personaHabilitada = mapper.createObjectNode();

			String tipoDeOferta = resultado.getString("TIPODEOFERTA");
			Long idPersonasHabilitada = resultado.getLong("IDPERSONAHABILITADA");
			String nombre = resultado.getString("NOMBRE");
			String email = resultado.getString("EMAIL");
			Long telefono = resultado.getLong("TELEFONO");

			personaHabilitada.put("TipoOferta", tipoDeOferta);
			personaHabilitada.put("Id", idPersonasHabilitada);
			personaHabilitada.put("Nombre:", nombre);
			personaHabilitada.put("Email", email);
			personaHabilitada.put("Telefono", telefono);

			personasHabilitadas.add(personaHabilitada);
		}

		return personasHabilitadas;
	}

	/**
	 * RFC11 - Mostrar los usuarios (persona habilitada) que NO han hecho reservas en el rango de fechas indicado y del tipo de oferta 
	 * definido  y ordenado por cualquier tipo de dato del cliente
	 * @param rango de fechas CON FORMATO '01/03/18'
	 * @return los usuarios que cumplen con los criterios
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getConsumoNegativoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(String fechaInicio, String fechaFinal, String tipoDeOferta, String datoOrdenar) throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode personasHabilitadas = mapper.createArrayNode();

		String sql = 
				String.format("SELECT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, \r\n" + 
						"PERSONAHABILITADA.EMAIL, PERSONAHABILITADA.TELEFONO \r\n" + 
						"FROM (%1$s.PERSONAHABILITADA LEFT JOIN\r\n" + 
						"(SELECT DISTINCT RESERVA.IDPERSONAHABILITADA ID_NO_MOSTRAR\r\n" + 
						"    FROM\r\n" + 
						"        %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA  \r\n" + 
						"        WHERE OFERTA.TIPODEOFERTA = %3$s \r\n" + 
						"AND RESERVA.FECHARESERVA>= %2$s AND RESERVA.FECHARESERVA<= %3$s) TABLAMALA\r\n" + 
						"\r\n" + 
						"ON PERSONAHABILITADA.IDPERSONAHABILITADA = TABLAMALA.ID_NO_MOSTRAR)\r\n" + 
						"WHERE TABLAMALA.ID_NO_MOSTRAR IS NULL\r\n" + 
						"ORDER BY PERSONAHABILITADA.%4$s;", USUARIO, fechaInicio, fechaFinal, tipoDeOferta, datoOrdenar);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();

		while(resultado.next()){
			ObjectNode personaHabilitada = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado.getLong("IDPERSONAHABILITADA");
			String nombre = resultado.getString("NOMBRE");
			String email = resultado.getString("EMAIL");
			Long telefono = resultado.getLong("TELEFONO");

			personaHabilitada.put("Id", idPersonasHabilitada);
			personaHabilitada.put("Nombre:", nombre);
			personaHabilitada.put("Email", email);
			personaHabilitada.put("Telefono", telefono);

			personasHabilitadas.add(personaHabilitada);
		}

		return personasHabilitadas;
	}

	/**
	 * RFC13 - Mostrar 
	 * los usuarios que han reservado en cada uno de los meses del año hasta el actual
	 * los usuarios que han reservado SOLO alojamientos costosos
	 * los usuarios que han reservado SOLO suites
	 * @return los usuarios que cumplen con los criterios
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getBuenosClientes() throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode personasHabilitadas = mapper.createArrayNode();

		//Obtengo usuarios que han reservado en cada uno de los meses del año hasta el actual
		String sql1 = 
				String.format("SELECT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, PERSONAHABILITADA.TELEFONO, \r\n" + 
						"\r\n" + 
						"PERSONAHABILITADA.EMAIL\r\n" + 
						"FROM (%1$s.PERSONAHABILITADA INNER JOIN (\r\n" + 
						"    SELECT ID_USUARIO, CANTIDAD_MESES \r\n" + 
						"    FROM (\r\n" + 
						"        SELECT DISTINCT RESERVA.IDPERSONAHABILITADA AS ID_USUARIO, COUNT (EXTRACT(MONTH FROM \r\n" + 
						"\r\n" + 
						"RESERVA.FECHAINICIORESERVA)) AS CANTIDAD_MESES \r\n" + 
						"        FROM %1$s.RESERVA GROUP BY  RESERVA.IDPERSONAHABILITADA )\r\n" + 
						"        WHERE CANTIDAD_MESES=4)\r\n" + 
						"    ON PERSONAHABILITADA.IDPERSONAHABILITADA = ID_USUARIO);", USUARIO);

		PreparedStatement prepStmt1 = conn.prepareStatement(sql1);
		ResultSet resultado1 = prepStmt1.executeQuery();

		while(resultado1.next()){
			ObjectNode personaHabilitada = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado1.getLong("IDPERSONAHABILITADA");
			String nombre = resultado1.getString("NOMBRE");
			Long telefono = resultado1.getLong("TELEFONO");
			String email = resultado1.getString("EMAIL");

			personaHabilitada.put("Calificacion", "ReservaCadaMes");
			personaHabilitada.put("Id", idPersonasHabilitada);
			personaHabilitada.put("Nombre:", nombre);
			personaHabilitada.put("Telefono", telefono);
			personaHabilitada.put("Email", email);

			personasHabilitadas.add(personaHabilitada);
		}


		//Obtengo usuarios que han reservado SOLO alojamientos costosos
		String sql2 = 
				String.format("SELECT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, PERSONAHABILITADA.TELEFONO, \r\n" + 
						"\r\n" + 
						"PERSONAHABILITADA.EMAIL\r\n" + 
						"FROM (%1$s.PERSONAHABILITADA \r\n" + 
						"        INNER JOIN (\r\n" + 
						"        SELECT ID_USUARIO2 AS ID_VALIOSO FROM (\r\n" + 
						"            (SELECT RESERVA.IDPERSONAHABILITADA AS ID_USUARIO2, COUNT(RESERVA.IDRESERVA) AS \r\n" + 
						"\r\n" + 
						"CANT_RESERVAS_VALIOSAS \r\n" + 
						"                FROM (\r\n" + 
						"                %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) WHERE OFERTA.VALOR >= 435000 \r\n" + 
						"                GROUP BY RESERVA.IDPERSONAHABILITADA\r\n" + 
						"            ) \r\n" + 
						"            INNER JOIN \r\n" + 
						"            (SELECT RESERVA.IDPERSONAHABILITADA AS ID_USUARIO1, COUNT(RESERVA.IDRESERVA) AS \r\n" + 
						"\r\n" + 
						"CANT_RESERVAS_TOTALES \r\n" + 
						"                FROM (\r\n" + 
						"                %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA)\r\n" + 
						"                GROUP BY RESERVA.IDPERSONAHABILITADA) THAT ON ID_USUARIO2= THAT.ID_USUARIO1)\r\n" + 
						"        WHERE CANT_RESERVAS_VALIOSAS = CANT_RESERVAS_TOTALES)\r\n" + 
						"    ON PERSONAHABILITADA.IDPERSONAHABILITADA= ID_VALIOSO);", USUARIO);

		PreparedStatement prepStmt2 = conn.prepareStatement(sql2);
		ResultSet resultado2 = prepStmt2.executeQuery();

		while(resultado2.next()){
			ObjectNode personaHabilitada = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado2.getLong("IDPERSONAHABILITADA");
			String nombre = resultado2.getString("NOMBRE");
			Long telefono = resultado2.getLong("TELEFONO");
			String email = resultado2.getString("EMAIL");

			personaHabilitada.put("Calificacion", "AlojamientosCostosos");
			personaHabilitada.put("Id", idPersonasHabilitada);
			personaHabilitada.put("Nombre:", nombre);
			personaHabilitada.put("Telefono", telefono);
			personaHabilitada.put("Email", email);

			personasHabilitadas.add(personaHabilitada);
		}

		//Obtengo usuario que han reservado SOLO alojamientos costosos
		String sql3 = 
				String.format("SELECT PERSONAHABILITADA.IDPERSONAHABILITADA, PERSONAHABILITADA.NOMBRE, PERSONAHABILITADA.TELEFONO, \r\n" + 
						"\r\n" + 
						"PERSONAHABILITADA.EMAIL\r\n" + 
						"FROM ( %1$s.PERSONAHABILITADA \r\n" + 
						"        INNER JOIN (\r\n" + 
						"        SELECT ID_USUARIO2 AS ID_VALIOSO FROM (\r\n" + 
						"            (SELECT RESERVA.IDPERSONAHABILITADA AS ID_USUARIO2, COUNT(RESERVA.IDRESERVA) AS \r\n" + 
						"\r\n" + 
						"CANT_RESERVAS_VALIOSAS \r\n" + 
						"                FROM (\r\n" + 
						"                %1$s.RESERVA INNER JOIN (\r\n" + 
						"                        SELECT OFERTA.IDOFERTA AS ID_OFERTA_SUITE FROM (\r\n" + 
						"                        %1$s.OFERTA INNER JOIN %1$s.OFERTAHOTEL ON OFERTA.IDOFERTA = OFERTAHOTEL.IDOFERTA) \r\n" + 
						"                        WHERE OFERTAHOTEL.CATEGORIA = 'suite') \r\n" + 
						"                ON RESERVA.IDOFERTA = ID_OFERTA_SUITE)\r\n" + 
						"                GROUP BY RESERVA.IDPERSONAHABILITADA\r\n" + 
						"            ) \r\n" + 
						"            INNER JOIN \r\n" + 
						"            (SELECT RESERVA.IDPERSONAHABILITADA AS ID_USUARIO1, COUNT(RESERVA.IDRESERVA) AS \r\n" + 
						"\r\n" + 
						"CANT_RESERVAS_TOTALES \r\n" + 
						"                FROM (\r\n" + 
						"                %1$s.RESERVA INNER JOIN %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA)\r\n" + 
						"                GROUP BY RESERVA.IDPERSONAHABILITADA) THAT ON ID_USUARIO2= THAT.ID_USUARIO1)\r\n" + 
						"        WHERE CANT_RESERVAS_VALIOSAS = CANT_RESERVAS_TOTALES)\r\n" + 
						"    ON PERSONAHABILITADA.IDPERSONAHABILITADA= ID_VALIOSO);", USUARIO);

		PreparedStatement prepStmt3 = conn.prepareStatement(sql3);
		ResultSet resultado3 = prepStmt3.executeQuery();

		while(resultado3.next()){
			ObjectNode personaHabilitada = mapper.createObjectNode();

			Long idPersonasHabilitada = resultado3.getLong("IDPERSONAHABILITADA");
			String nombre = resultado3.getString("NOMBRE");
			Long telefono = resultado3.getLong("TELEFONO");
			String email = resultado3.getString("EMAIL");

			personaHabilitada.put("Calificacion", "SoloSuites");
			personaHabilitada.put("Id", idPersonasHabilitada);
			personaHabilitada.put("Nombre:", nombre);
			personaHabilitada.put("Telefono", telefono);
			personaHabilitada.put("Email", email);

			personasHabilitadas.add(personaHabilitada);
		}


		return personasHabilitadas;
	}
}

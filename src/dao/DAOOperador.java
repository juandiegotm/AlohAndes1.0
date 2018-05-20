package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import vos.Oferta;
import vos.Operador;
import vos.PersonaHabilitada;

public class DAOOperador extends DAOAlohAndes{

	public ArrayNode ingresosDeOperadores() throws SQLException, Exception{
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode operadores = mapper.createArrayNode();
		
		String sql = String.format("SELECT OPERADOR.IDOPERADOR, OPERADOR.NOMBRE, SUM(OFERTA.VALOR) AS INGRESOS "
				+ "FROM  %1$s.OPERADOR INNER JOIN ( %1$s.RESERVA INNER JOIN  %1$s.OFERTA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA) ON OPERADOR.IDOPERADOR = OFERTA.IDOPERADOR "
				+ "WHERE RESERVA.ESTADORESERVA='finalizado' GROUP BY OPERADOR.IDOPERADOR, OPERADOR.NOMBRE", USUARIO);

		PreparedStatement query = conn.prepareStatement(sql);
		ResultSet resultado = query.executeQuery();

		recursos.add(query);

		while(resultado.next()){
			operadores.add(convetirResultSet(resultado, mapper));
		}
		return operadores;
	}
	
	
	public ObjectNode convetirResultSet(ResultSet resultado, ObjectMapper mapper) throws SQLException {

		ObjectNode operador = mapper.createObjectNode();
		Long id = resultado.getLong("IDOPERADOR");
		String nombre = resultado.getString("NOMBRE");
		Double valor = resultado.getDouble("INGRESOS");
		
		operador.put("ID", id);
		operador.put("Nombre", nombre);
		operador.put("Ingresos", valor);
		
		return operador;

	}
	
	/**
	 * RFC12_PARTE3 - Mostrar para cada semana del año los operadores MAS solicitados
	 * @param id del usuario de AlohAndes que se quiere consultar
	 * @return el uso de AlohAndes para el usuario con el id dado por parametro
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getSemanasConOperadoresMasSolicitados() throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode operadores = mapper.createArrayNode();

		String sql = 
				String.format("SELECT NRO_SEMANA, OPERADOR.IDOPERADOR, OPERADOR.NOMBRE, OPERADOR.DIRECCION, OPERADOR.TELEFONO, \r\n" + 
						"\r\n" + 
						"OPERADOR.TIPODEOPERADOR, CANTIDAD\r\n" + 
						"    FROM (%1$s.OPERADOR INNER JOIN (\r\n" + 
						"    SELECT  NRO_SEMANA, OFERTA.IDOFERTA, OFERTA.IDOPERADOR AS OPERADOR_ID, CANT AS CANTIDAD\r\n" + 
						"        FROM( %1$s.OFERTA INNER JOIN (\r\n" + 
						"            SELECT SEMANA_INICIO AS NRO_SEMANA, OFER AS OFERTA_ID, CANT\r\n" + 
						"            FROM(\r\n" + 
						"                    SELECT SEMANA_INICIO, OFER,ROW_NUMBER() OVER(PARTITION BY SEMANA_INICIO ORDER BY CANT DESC) \r\n" + 
						"\r\n" + 
						"AS POSICION, CANT\r\n" + 
						"                    FROM(\r\n" + 
						"                        SELECT TO_NUMBER(TO_CHAR(TO_DATE( CAST(RESERVA.FECHAINICIORESERVA AS DATE), \r\n" + 
						"\r\n" + 
						"'DD/MM/YY'),'WW')) AS SEMANA_INICIO, \r\n" + 
						"                        RESERVA.IDOFERTA AS OFER, RESERVA.CANTIDAD AS CANT\r\n" + 
						"                        FROM %1$s.RESERVA\r\n" + 
						"                        ORDER BY SEMANA_INICIO\r\n" + 
						"                    )\r\n" + 
						"            )\r\n" + 
						"        WHERE POSICION = 1)\r\n" + 
						"        ON OFERTA.IDOFERTA= OFERTA_ID)\r\n" + 
						"        ) ON OPERADOR.IDOPERADOR = OPERADOR_ID)\r\n" + 
						"ORDER BY NRO_SEMANA;", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();

		while(resultado.next()){
			ObjectNode operador = mapper.createObjectNode();

			Integer nroSemana = resultado.getInt("NRO_SEMANA");
			Long idOperador= resultado.getLong("IDOPERADOR");
			String nombre = resultado.getString("NOMBRE");
			String direccion = resultado.getString("DIRECCION");
			Integer telefono = resultado.getInt("TELEFONO");
			String tipoDeOperador = resultado.getString("TIPODEOPERADOR");

			operador.put("NroSemana", nroSemana);
			operador.put("IdOperador:", idOperador);
			operador.put("Nombre", nombre);
			operador.put("Direccion", direccion);
			operador.put("Telefono", telefono);
			operador.put("TipoDeOperador", tipoDeOperador);
	
			operadores.add(operador);
		}

		return operadores;
	}

	
	
	/**
	 * RFC12_PARTE4 - Mostrar para cada semana del año los operadores MENOS solicitados
	 * @param id del usuario de AlohAndes que se quiere consultar
	 * @return el uso de AlohAndes para el usuario con el id dado por parametro
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayNode getSemanasConOperadoresMenosSolicitados() throws SQLException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode operadores = mapper.createArrayNode();

		String sql = 
				String.format("SELECT NRO_SEMANA, OPERADOR.IDOPERADOR, OPERADOR.NOMBRE, OPERADOR.DIRECCION, OPERADOR.TELEFONO, \r\n" + 
						"\r\n" + 
						"OPERADOR.TIPODEOPERADOR, CANTIDAD\r\n" + 
						"    FROM (%1$s.OPERADOR INNER JOIN (\r\n" + 
						"    SELECT  NRO_SEMANA, OFERTA.IDOFERTA, OFERTA.IDOPERADOR AS OPERADOR_ID, CANT AS CANTIDAD\r\n" + 
						"        FROM( %1$s.OFERTA INNER JOIN (\r\n" + 
						"            SELECT SEMANA_INICIO AS NRO_SEMANA, OFER AS OFERTA_ID, CANT\r\n" + 
						"            FROM(\r\n" + 
						"                    SELECT SEMANA_INICIO, OFER,ROW_NUMBER() OVER(PARTITION BY SEMANA_INICIO ORDER BY CANT ASC) \r\n" + 
						"\r\n" + 
						"AS POSICION, CANT\r\n" + 
						"                    FROM(\r\n" + 
						"                        SELECT TO_NUMBER(TO_CHAR(TO_DATE( CAST(RESERVA.FECHAINICIORESERVA AS DATE), \r\n" + 
						"\r\n" + 
						"'DD/MM/YY'),'WW')) AS SEMANA_INICIO, \r\n" + 
						"                        RESERVA.IDOFERTA AS OFER, RESERVA.CANTIDAD AS CANT\r\n" + 
						"                        FROM %1$s.RESERVA\r\n" + 
						"                        ORDER BY SEMANA_INICIO\r\n" + 
						"                    )\r\n" + 
						"            )\r\n" + 
						"        WHERE POSICION = 1)\r\n" + 
						"        ON OFERTA.IDOFERTA= OFERTA_ID)\r\n" + 
						"        ) ON OPERADOR.IDOPERADOR = OPERADOR_ID)\r\n" + 
						"ORDER BY NRO_SEMANA;", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet resultado = prepStmt.executeQuery();

		while(resultado.next()){
			ObjectNode operador = mapper.createObjectNode();

			Integer nroSemana = resultado.getInt("NRO_SEMANA");
			Long idOperador= resultado.getLong("IDOPERADOR");
			String nombre = resultado.getString("NOMBRE");
			String direccion = resultado.getString("DIRECCION");
			Integer telefono = resultado.getInt("TELEFONO");
			String tipoDeOperador = resultado.getString("TIPODEOPERADOR");

			operador.put("NroSemana", nroSemana);
			operador.put("IdOperador:", idOperador);
			operador.put("Nombre", nombre);
			operador.put("Direccion", direccion);
			operador.put("Telefono", telefono);
			operador.put("TipoDeOperador", tipoDeOperador);

			operadores.add(operador);
		}

		return operadores;
	}
}

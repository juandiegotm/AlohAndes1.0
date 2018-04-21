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

}

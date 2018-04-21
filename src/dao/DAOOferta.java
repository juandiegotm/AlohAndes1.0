package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import vos.Oferta;
import vos.PersonaHabilitada;

public class DAOOferta extends DAOAlohAndes {

	
	
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
	
	public void retirarOferta(Long idOferta) throws SQLException, Exception  {
		
		String sql = String.format("DELETE FROM %1$s.OFERTA WHERE IDOFERTA = %2$d", USUARIO, idOferta);

		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	

	public ArrayNode getOfertasMasPopulares() throws SQLException, Exception{
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ofertas = mapper.createArrayNode();

		String sql = "SELECT TOP_POPULA, IDOFERTA, TIPODEOFERTA, DISPONIBLE, VALOR, FECHA_INICIO, FECHA_FINAL "
				+ "FROM( "
				+ "SELECT OFERTA.IDOFERTA AS IDOFERTA, OFERTA.TIPODEOFERTA AS TIPODEOFERTA, OFERTA.CANTIDADDISPONIBLE AS DISPONIBLE, OFERTA.VALOR AS VALOR,OFERTA.FECHAINICIO AS FECHA_INICIO,OFERTA.FECHAFINAL AS FECHA_FINAL, COUNT(OFERTA.IDOFERTA) AS RESERVAS ,ROW_NUMBER() OVER (ORDER BY  COUNT (OFERTA.IDOFERTA) DESC)AS TOP_POPULA "
				+ String.format("FROM  %1$s.OFERTA INNER JOIN %1$s.RESERVA ON RESERVA.IDOFERTA = OFERTA.IDOFERTA ", USUARIO)
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
	
	
	public ArrayNode getIndiceOcupacionPorOferta() throws Exception, SQLException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ofertas = mapper.createArrayNode();
		
		String sql = String.format("SELECT OFERTA.IDOFERTA, 100-(100/OFERTA.CANTIDADINICIAL)*OFERTA.CANTIDADDISPONIBLE AS PORCENTAJE_OCUPACION\r\n" + 
				"FROM %1$s.OFERTA ORDER BY PORCENTAJE_OCUPACION DESC", USUARIO);
		
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
	
	
	
}

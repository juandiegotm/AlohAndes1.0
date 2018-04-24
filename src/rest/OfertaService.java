package rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import tm.AlohAndesTransactionManager;
import vos.Oferta;

@Path("/ofertas")
public class OfertaService extends AlohAndesService{


	// ----------------------------------------------------------------------------------------------------------------------------------
	// METODOS REST
	// ----------------------------------------------------------------------------------------------------------------------------------
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllOfertas() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			List<Oferta> ofertas = tm.getAllOferta();

			return Response.status(200).entity(ofertas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{id: \\d+}")
	public Response findOfertaById(@PathParam("id") Long id) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			Oferta oferta = tm.buscarOfertaPorId(id);

			if (oferta == null) {
				throw new Exception("La oferta buscada no existe");
			}

			return Response.status(200).entity(oferta).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("ofertasMasPopulares")
	public Response getOfertasMasPopulares() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode ofertas = tm.getOfertasMasPopulares();

			return Response.status(200).entity(ofertas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@DELETE
	@Path("{idOferta: \\d+}")
	public Response eliminarReserva(@PathParam("idOferta") Long idOferta) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			tm.retirarOferta(idOferta);

			return Response.status(200).build();
		}

		catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Path("/indiceOcupacion")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getIndiceOcupacionPorOferta() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			ArrayNode ofertas = tm.getIndiceOcupacionPorOferta();

			return Response.status(200).entity(ofertas).build();
		}

		catch(Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@POST
	@Path("/alojamientoPorFechasYServicios")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAlojamientosPorFechasYServicios(ObjectNode json){
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			String fechaInicioText = json.get("fechaInicio").getValueAsText();
			String fechaFinalText = json.get("fechaInicio").getValueAsText();
			ArrayNode arrayServicios = (ArrayNode) json.get("servicios");
			List<String> servicios = new ArrayList<>();
			
			for(JsonNode serviceNode: arrayServicios) {
				servicios.add(serviceNode.getValueAsText());
			}
			
			System.out.println(servicios.toString());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			Date fechaInicio = formatter.parse(fechaInicioText);
			Date fechaFinal = formatter.parse(fechaFinalText);

			List<Oferta> ofertas = tm.getOfertasEnRangoDeFechasYCiertosServicios(fechaInicio, fechaFinal, servicios);
			
			return Response.status(200).entity(ofertas).build();
		}
		
		catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/ofertasConPocaDemanda")
	public Response getOfertasConPocaDemanda() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			List<Oferta> ofertas = tm.getOfertasConPocaDemanda();

			return Response.status(200).entity(ofertas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

}

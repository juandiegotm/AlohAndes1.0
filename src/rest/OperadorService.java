package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.node.ArrayNode;

import tm.AlohAndesTransactionManager;

@Path("/operadores")
public class OperadorService extends AlohAndesService {

	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("dineroRecibido")
	public Response getOperadoresDinero() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode personas = tm.getOperadorDinero();
		
			return Response.status(200).entity(personas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	//RFC12_Parte3
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/semanasConOperadoresMasSolicitados")
	public Response getSemanasConOperadoresMasSolicitados() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode semanasConOperadoresMasSolicitados = tm.getSemanasConOperadoresMasSolicitados();

			return Response.status(200).entity(semanasConOperadoresMasSolicitados).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	//RFC12_Parte4
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/semanasConOperadoresMenosSolicitados")
	public Response getSemanasConOperadoresMenosSolicitados() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode semanasConOperadoresMenosSolicitados = tm.getSemanasConOperadoresMenosSolicitados();

			return Response.status(200).entity(semanasConOperadoresMenosSolicitados).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
}

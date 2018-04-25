package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import tm.AlohAndesTransactionManager;
import vos.Oferta;

@Path("/reservas")
public class ReservaService extends AlohAndesService{


	// ----------------------------------------------------------------------------------------------------------------------------------
	// METODOS REST
	// ----------------------------------------------------------------------------------------------------------------------------------
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/consultarPorPeriodo")
	public Response getOperacionDeAlohAndes(@QueryParam("periodo") String periodo, @QueryParam("tipoOferta") String tipoOferta) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode operacionDeAlohAndes = tm.getOperacionDeAlohAndes(periodo, tipoOferta);

			return Response.status(200).entity(operacionDeAlohAndes).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
}

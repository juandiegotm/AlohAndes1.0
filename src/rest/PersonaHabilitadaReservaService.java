package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohAndesTransactionManager;
import vos.Reserva;

public class PersonaHabilitadaReservaService extends AlohAndesService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{idOferta: \\d+}")
	public Response crearReserva(Reserva reserva, @PathParam("idPersona") Long idPersona, @PathParam("idOferta") Long idOferta) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			tm.crearReserva(idPersona, idOferta, reserva);			
			return Response.status(200).build();
		}
		
		catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@DELETE
	@Path("/{idReserva: \\d+}")
	public Response eliminarReserva(@PathParam("idPersona") Long idPersona, @PathParam("idReserva") Long idReserva) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			tm.eliminarReserva(idPersona, idReserva);
			return Response.status(200).build();
		}
		
		catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("/funciona")
	public Response funcion() {
	   	return Response.status(200).entity("Hola mundo").build();
	}
}

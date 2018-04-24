package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.node.ArrayNode;

import tm.AlohAndesTransactionManager;
import vos.Oferta;
import vos.PersonaHabilitada;
import vos.Reserva;

@Path("/personasHabilitadas")
public class PersonaHabilitadaService extends AlohAndesService {

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS REST
	//----------------------------------------------------------------------------------------------------------------------------------
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPersonasHabilitadas() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			List<PersonaHabilitada> personas = tm.getAllBPersonasHabilitadas();

			return Response.status(200).entity(personas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addPersonaHabilitada(PersonaHabilitada persona) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			tm.agregarPersonaHabilitada(persona);

			return Response.status(200).build();

		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{id: \\d+}")
	public Response findPersonaHabilitadaById(@PathParam("id") Long id) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			PersonaHabilitada persona = tm.buscarPersonaHabilitadaPorId(id);

			if (persona == null) {
				throw new Exception("La persona buscada no existe");
			}

			return Response.status(200).entity(persona).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@Path("{idPersona: \\d+}/reservas")
	public PersonaHabilitadaReservaService reservas() {
		PersonaHabilitadaReservaService instancia =  new PersonaHabilitadaReservaService();
		instancia.setContext(this.context);
		return instancia;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/clientesFrecuentes")
	public Response getClientesFrecuentes() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			List<PersonaHabilitada> clientesFrecuentes = tm.getClientesFrecuentes();

			return Response.status(200).entity(clientesFrecuentes).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/usoAlohAndes")
	public Response getUsoDeAlohAndesPorCadaUsuario() {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode personas = tm.getUsoDeAlohAndesPorCadaUsuario();

			return Response.status(200).entity(personas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}


		
//		@POST
//		@Path("{idPersona: \\d+}/reservas/{idOferta: \\d+}")
//		@Consumes(MediaType.APPLICATION_JSON)
//	    public Response crearReserva(Reserva reserva, @PathParam("idPersona") Long idPersona, @PathParam("idOferta")Long idOferta) {
//	        try {
//	            ReservaTransactionManager tm = new ReservaTransactionManager(getPath());
//	
//	            tm.crearReserva(idPersona, idOferta, reserva);
//	
//	            return Response.status(200).build();
//	        } catch (Exception e) {
//	            return Response.status(500).entity(doErrorMessage(e)).build();
//	        }
//	    }
//	    
//	    @GET
//	    @Path("funciona")
//	    @Produces("text/plain")
//	    public Response funcion() {
//	    	return Response.status(200).entity("Hola mundo").build();
//	    }

}

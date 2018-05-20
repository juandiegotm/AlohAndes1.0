package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
	@Path("/clientesFrecuentes/{idOferta: \\d+}")
	public Response getClientesFrecuentes(@PathParam("idOferta") Long idOferta) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			List<PersonaHabilitada> clientesFrecuentes = tm.getClientesFrecuentes(idOferta);

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
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/usoAlohAndes/{idPersona: \\d+}")
	public Response getUsoDeAlohAndesParaUsuarioEspecifico(@PathParam("idPersona") Long idPersona) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode personas = tm.getUsoDeAlohAndesPorUsuarioEspecifico(idPersona);

			return Response.status(200).entity(personas).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}		
	
	//RFC10_Parte1
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/consumoAlohAndesFechas-Oferta-Orden")
	public Response getConsumoDeAlohAndesParte1(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFinal") String fechaFinal,
			@QueryParam("tipoOferta") String tipoOferta, @QueryParam("orden") String orden) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode consumoAlohAndesFechaOfertaOrden = tm.getConsumoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(fechaInicio, fechaFinal, tipoOferta, orden);

			return Response.status(200).entity(consumoAlohAndesFechaOfertaOrden).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	//RFC10_Parte2
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/consumoAlohAndesFechas-AgrupadoOferta")
	public Response getConsumoDeAlohAndesParte2(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFinal") String fechaFinal) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode consumoAlohAndesFechasAgrupadoOferta = tm.getConsumoDeAlohAndesDadoRangoFechasAgrupadoTipoOferta(fechaInicio, fechaFinal);

			return Response.status(200).entity(consumoAlohAndesFechasAgrupadoOferta).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	//RFC11
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/consumoNegativoAlohAndesFechas-Oferta-Orden")
	public Response getConsumoNegativoDeAlohAndes(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFinal") String fechaFinal,
			@QueryParam("tipoOferta") String tipoOferta, @QueryParam("orden") String orden) {
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
			ArrayNode consumoNegativoAlohAndesFechaOfertaOrden = tm.getConsumoNegativoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(fechaInicio, fechaFinal, tipoOferta, orden);

			return Response.status(200).entity(consumoNegativoAlohAndesFechaOfertaOrden).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	//RFC13
		@GET
		@Produces({ MediaType.APPLICATION_JSON })
		@Path("/consultarBuenosClientes")
		public Response getBuenosClientes() {
			try {
				AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
				ArrayNode buenosClientes = tm.getBuenosClientes();

				return Response.status(200).entity(buenosClientes).build();
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		}

}

package vos;



import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;




public class Reserva {
	
	public final static String PAGADO = "PAGADO";
	public final static String ESPERANDO_PAGO = "ESPERANDO PAGO";
	public final static String CANCELADO = "CANCELADO";
	public final static String EN_CURSO = "EN CURSO";
	public final static String FINALIZADO = "FINALIZADO";
	
	@JsonProperty(value = "idReserva")
	private Long idReserva;
	
	@JsonProperty(value = "fechaReserva")
	//@JsonFormat(pattern =  "dd-mm-yyyy")
	private Date fechaReserva;
	
	@JsonProperty(value = "estadoReserva")
	private String estadoReserva;
	
	private PersonaHabilitada personaHabilitada;
	
	private Oferta oferta;
	
	public Reserva() {
		//Dummy constructor
	}
	
	public Reserva(@JsonProperty(value = "idReserva") Long idReserva, @JsonProperty(value = "fechaReserva") Date fechaReserva, 
			@JsonProperty(value = "estadoReserva") String estadoReserva) {
		this.idReserva = idReserva;
		this.fechaReserva = fechaReserva;
		this.estadoReserva = estadoReserva;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public String getEstadoReserva() {
		return estadoReserva;
	}

	public void setEstadoReserva(String estadoReserva) {
		this.estadoReserva = estadoReserva;
	}

	public PersonaHabilitada getPersonaHabilitada() {
		return personaHabilitada;
	}

	public void setPersonaHabilitada(PersonaHabilitada personaHabilitada) {
		this.personaHabilitada = personaHabilitada;
	}

	public Long getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}
	
	

	
}

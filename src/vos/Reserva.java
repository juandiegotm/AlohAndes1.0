package vos;



import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;



public class Reserva {
	
	public final static String PAGADO = "PAGADO";
	public final static String ESPERANDO_PAGO = "ESPERANDO PAGO";
	public final static String CANCELADO = "CANCELADO";
	public final static String EN_CURSO = "EN CURSO";
	public final static String FINALIZADO = "FINALIZADO";
	public final static String EN_ESPERA = "EN_ESPERA";
	
	@JsonProperty(value = "idReserva")
	private Long idReserva;
	
	@JsonProperty(value = "cantidad")
	private  Integer cantidad;
	
	@JsonProperty(value = "fechaInicioReserva")
	private Date fechaInicioReserva;
	
	@JsonProperty(value = "fechaFinalReserva")
	private Date fechaFinalReserva;
	
	@JsonProperty(value = "estadoReserva")
	private String estadoReserva;
	
	private PersonaHabilitada personaHabilitada;
	
	private Oferta oferta;
	
	public Reserva() {
		//Dummy constructor
	}
	
	public Reserva(@JsonProperty(value = "idReserva") Long idReserva, @JsonProperty(value = "fechaInicioReserva") Date fechaInicioReserva,
			@JsonProperty(value = "fechaFinalReserva") Date fechaFinalReserva,
			@JsonProperty(value = "estadoReserva") String estadoReserva, @JsonProperty(value = "cantidad") Integer cantidad) {
		this.idReserva = idReserva;
		this.fechaInicioReserva = fechaInicioReserva;
		this.fechaFinalReserva = fechaFinalReserva;
		this.estadoReserva = estadoReserva;
		this.cantidad = cantidad;
	}

	public Date getFechaInicioReserva() {
		return fechaInicioReserva;
	}

	public void setFechaInicioReserva(Date fechaReserva) {
		this.fechaInicioReserva = fechaReserva;
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

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaFinalReserva() {
		return fechaFinalReserva;
	}

	public void setFechaFinalReserva(Date fechaFinalReserva) {
		this.fechaFinalReserva = fechaFinalReserva;
	}
	
	
	
	
	

	
}

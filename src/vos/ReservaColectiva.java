package vos;

import java.sql.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReservaColectiva {
	@JsonProperty(value = "idReservaColectiva")
	private Long idReservaColectiva;
	
	@JsonProperty(value = "fechaReserva")
	private Date fechaReserva;
	
	@JsonProperty(value = "estadoReserva")
	private String estadoReserva;
	
	@JsonProperty(value = "cantidad")
	private Integer cantidad;
	
	@JsonProperty(value = "servicios")
	private List<String> servicios;
	
	@JsonProperty(value = "tipoDeAlojamiento")
	private String tipoDeAlojamiento;
	
	@JsonProperty(value = "idPersonaHabilitda")
	private Long idPersonaHabilitada;

	/**
	 * Constructor por defecto de la clase ReservaColectiva
	 * @param idReservaColectiva
	 * @param fechaReserva
	 * @param estadoReserva
	 * @param cantidad
	 * @param servicios
	 */
	public ReservaColectiva(@JsonProperty(value = "idReservaColectiva")Long idReservaColectiva, @JsonProperty(value = "fechaReserva") Date fechaReserva, @JsonProperty(value = "estadoReserva") String estadoReserva, 
			@JsonProperty(value = "cantidad") Integer cantidad, @JsonProperty(value = "servicios")List<String> servicios, @JsonProperty(value = "tipoDeAlojamiento") String tipoDeAlojamiento,
			@JsonProperty(value = "idPersonaHabilitada") Long idPersonaHabilitada) {
		this.idReservaColectiva = idReservaColectiva;
		this.fechaReserva = fechaReserva;
		this.estadoReserva = estadoReserva;
		this.cantidad = cantidad;
		this.servicios = servicios;
		this.tipoDeAlojamiento = tipoDeAlojamiento;
		this.idPersonaHabilitada = idPersonaHabilitada;
	}
	
	/**
	 * Constructor vacio de la clase Reserva Colectiva
	 */
	public ReservaColectiva() {
		//Empty constructor
	}

	/**
	 * @return the idReservaColectiva
	 */
	public Long getIdReservaColectiva() {
		return idReservaColectiva;
	}

	/**
	 * @param idReservaColectiva the idReservaColectiva to set
	 */
	public void setIdReservaColectiva(Long idReservaColectiva) {
		this.idReservaColectiva = idReservaColectiva;
	}

	/**
	 * @return the fechaReserva
	 */
	public Date getFechaReserva() {
		return fechaReserva;
	}

	/**
	 * @param fechaReserva the fechaReserva to set
	 */
	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	/**
	 * @return the estadoReserva
	 */
	public String getEstadoReserva() {
		return estadoReserva;
	}

	/**
	 * @param estadoReserva the estadoReserva to set
	 */
	public void setEstadoReserva(String estadoReserva) {
		this.estadoReserva = estadoReserva;
	}

	/**
	 * @return the cantidad
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the servicios
	 */
	public List<String> getServicios() {
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(List<String> servicios) {
		this.servicios = servicios;
	}

	/**
	 * @return the tipoDeAlojamiento
	 */
	public String getTipoDeAlojamiento() {
		return tipoDeAlojamiento;
	}

	/**
	 * @param tipoDeAlojamiento the tipoDeAlojamiento to set
	 */
	public void setTipoDeAlojamiento(String tipoDeAlojamiento) {
		this.tipoDeAlojamiento = tipoDeAlojamiento;
	}

	/**
	 * @return the idPersonaHabilitada
	 */
	public Long getIdPersonaHabilitada() {
		return idPersonaHabilitada;
	}

	/**
	 * @param idPersonaHabilitada the idPersonaHabilitada to set
	 */
	public void setIdPersonaHabilitada(Long idPersonaHabilitada) {
		this.idPersonaHabilitada = idPersonaHabilitada;
	}
	
	
	
	
	
	
	
	
	
	
	
}

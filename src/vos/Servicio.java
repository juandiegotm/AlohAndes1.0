package vos;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Servicio {

	@JsonProperty(value = "idOferta")
	protected Long idOferta;
	
	@JsonProperty(value = "valor")
	protected Double valor;
	
	@JsonProperty(value = "duracion")
	protected String duracion;
	
	@JsonProperty(value = "fechaInicio")
	protected Date fechaInicio;
	
	@JsonProperty(value = "fechaFinal")
	protected Date fechaFinal;
	
	@JsonProperty(value = "cantidadDisponible")
	protected Integer cantidadDisponible;
	
	@JsonProperty(value = "direccion")
	protected String direccion;
	
	@JsonProperty(value = "tipoDeOferta")
	protected String tipoDeOferta;
	
	@JsonProperty(value = "cantidadIncial")
	protected Integer cantidadInicial;
	
	protected List<Servicio> servicio;

	
	/**
	 * @param idOferta
	 * @param valor
	 * @param duracion
	 * @param fechaInicio
	 * @param fechaFinal
	 * @param cantidadDisponible
	 * @param direccion
	 * @param tipoDeOferta
	 * @param cantidadInicial
	 */
	public Servicio(@JsonProperty(value = "idOferta") Long idOferta, @JsonProperty(value = "valor") Double valor, @JsonProperty(value = "duracion") String duracion, @JsonProperty(value = "fechaInicio") Date fechaInicio, 
			@JsonProperty(value = "fechaFinal") Date fechaFinal, @JsonProperty(value = "cantidadDisponible") Integer cantidadDisponible, @JsonProperty(value = "direccion") String direccion, 
			@JsonProperty(value = "tipoDeOferta") String tipoDeOferta, @JsonProperty(value = "cantidadIncial") Integer cantidadInicial) 
	{
		this.idOferta = idOferta;
		this.valor = valor;
		this.duracion = duracion;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.cantidadDisponible = cantidadDisponible;
		this.direccion = direccion;
		this.tipoDeOferta = tipoDeOferta;
		this.cantidadInicial = cantidadInicial;
	}
	
	public Servicio() {
		//Dummy constructor
	}

	public Long getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public Integer getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(Integer cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipoDeOferta() {
		return tipoDeOferta;
	}

	public void setTipoDeOferta(String tipoDeOferta) {
		this.tipoDeOferta = tipoDeOferta;
	}

	public Integer getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(Integer cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}

	public List<Servicio> getServicio() {
		return servicio;
	}

	public void setServicio(List<Servicio> servicio) {
		this.servicio = servicio;
	}
	
	
	
	
	
	
	
	
}

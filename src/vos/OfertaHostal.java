package vos;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonProperty;

public class OfertaHostal extends Oferta{
	
	@JsonProperty(value = "categoria")
	private String categoria;
	
	@JsonProperty(value = "capacidadPersonas")
	private Integer capacidadPersonas;
	
	@JsonProperty(value = "ubicacion")
	private String ubicacion;
	
	@JsonProperty(value = "tamanoEnMetros")
	private Double tamanoEnMetros;
	
	
	public OfertaHostal(@JsonProperty(value = "idOferta") Long idOferta, @JsonProperty(value = "valor") Double valor, @JsonProperty(value = "duracion") String duracion, @JsonProperty(value = "fechaInicio") Timestamp fechaInicio, 
			@JsonProperty(value = "fechaFinal") Timestamp fechaFinal, @JsonProperty(value = "cantidadDisponible") Integer cantidadDisponible, @JsonProperty(value = "direccion") String direccion, 
			@JsonProperty(value = "tipoDeOferta") String tipoDeOferta, @JsonProperty(value = "cantidadIncial") Integer cantidadInicial, @JsonProperty(value = "categoria") String categoria,
			@JsonProperty(value = "capacidadPersonas") Integer capacidadPersonas, @JsonProperty(value = "ubicacion") String ubicacion, @JsonProperty(value = "tamanoEnMetros") Double tamanoEnMetros) {
		
		super(idOferta, valor, duracion, fechaInicio, fechaFinal, cantidadDisponible, direccion, tipoDeOferta, cantidadInicial);
		this.categoria = categoria;
		this.capacidadPersonas = capacidadPersonas;
		this.ubicacion = ubicacion;
		this.tamanoEnMetros = tamanoEnMetros;
		
	}
	
	public OfertaHostal() {
		
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Integer getCapacidadPersonas() {
		return capacidadPersonas;
	}

	public void setCapacidadPersonas(Integer capacidadPersonas) {
		this.capacidadPersonas = capacidadPersonas;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Double getTamanoEnMetros() {
		return tamanoEnMetros;
	}

	public void setTamanoEnMetros(Double tamanoEnMetros) {
		this.tamanoEnMetros = tamanoEnMetros;
	}
	
	 
	

}

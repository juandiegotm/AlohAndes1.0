package vos;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonProperty;

public class OfertaViviendaEsporadica extends Oferta {
	
	@JsonProperty(value = "ubicacion")
	private String ubicacion;
	
	@JsonProperty(value = "menaje")
	private String menaje;
	
	public OfertaViviendaEsporadica(@JsonProperty(value = "idOferta") Long idOferta, @JsonProperty(value = "valor") Double valor, @JsonProperty(value = "duracion") String duracion, @JsonProperty(value = "fechaInicio") Timestamp fechaInicio, 
			@JsonProperty(value = "fechaFinal") Timestamp fechaFinal, @JsonProperty(value = "cantidadDisponible") Integer cantidadDisponible, @JsonProperty(value = "direccion") String direccion, 
			@JsonProperty(value = "tipoDeOferta") String tipoDeOferta, @JsonProperty(value = "cantidadIncial") Integer cantidadInicial, @JsonProperty(value = "ubicacion") String ubicacion,
			@JsonProperty(value = "menaje") String menaje) {
		super(idOferta, valor, duracion, fechaInicio, fechaFinal, cantidadDisponible, direccion, tipoDeOferta, cantidadInicial);
		this.ubicacion = ubicacion;
		this.menaje = menaje;
	}
	
	public OfertaViviendaEsporadica() {
		//Empty constructor
	}

	/**
	 * @return the ubicacion
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * @return the menaje
	 */
	public String getMenaje() {
		return menaje;
	}

	/**
	 * @param menaje the menaje to set
	 */
	public void setMenaje(String menaje) {
		this.menaje = menaje;
	}
	
	
	

}

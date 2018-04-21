package vos;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonProperty;

public class OfertaViviendaUniversitaria extends Oferta {
	
	@JsonProperty(value = "tipoHabitacion")
	private String tipoHabitacion;
	
	@JsonProperty(value = "capacidadPersonas")
	private Integer capacidadPersonas;
	
	@JsonProperty(value = "ubicacionHabitacion")
	private String ubicacionHabitacion;
	
	@JsonProperty(value = "menajeHabitacion")
	private String menajeHabitacion;
	
	public OfertaViviendaUniversitaria(@JsonProperty(value = "idOferta") Long idOferta, @JsonProperty(value = "valor") Double valor, @JsonProperty(value = "duracion") String duracion, @JsonProperty(value = "fechaInicio") Timestamp fechaInicio, 
			@JsonProperty(value = "fechaFinal") Timestamp fechaFinal, @JsonProperty(value = "cantidadDisponible") Integer cantidadDisponible, @JsonProperty(value = "direccion") String direccion, 
			@JsonProperty(value = "tipoDeOferta") String tipoDeOferta, @JsonProperty(value = "cantidadIncial") Integer cantidadInicial, @JsonProperty(value = "tipoHabitacion") String tipoHabitacion,
			@JsonProperty(value = "capacidadPersonas") Integer capacidadPersonas, @JsonProperty(value = "ubicacionHabitacion") String ubicacionHabitacion,
			@JsonProperty(value = "menajeHabitacion") String menajeHabitacion) {
		super(idOferta, valor, duracion, fechaInicio, fechaFinal, cantidadDisponible, direccion, tipoDeOferta, cantidadInicial);
		this.tipoHabitacion = tipoHabitacion;
		this.capacidadPersonas = capacidadPersonas;
		this.ubicacionHabitacion = ubicacionHabitacion;
		this.menajeHabitacion = menajeHabitacion;
	}
	
	public OfertaViviendaUniversitaria() {
		//Empty constructor
	}

	/**
	 * @return the tipoHabitacion
	 */
	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	/**
	 * @param tipoHabitacion the tipoHabitacion to set
	 */
	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	/**
	 * @return the capacidadPersonas
	 */
	public Integer getCapacidadPersonas() {
		return capacidadPersonas;
	}

	/**
	 * @param capacidadPersonas the capacidadPersonas to set
	 */
	public void setCapacidadPersonas(Integer capacidadPersonas) {
		this.capacidadPersonas = capacidadPersonas;
	}

	/**
	 * @return the ubicacionHabitacion
	 */
	public String getUbicacionHabitacion() {
		return ubicacionHabitacion;
	}

	/**
	 * @param ubicacionHabitacion the ubicacionHabitacion to set
	 */
	public void setUbicacionHabitacion(String ubicacionHabitacion) {
		this.ubicacionHabitacion = ubicacionHabitacion;
	}

	/**
	 * @return the menajeHabitacion
	 */
	public String getMenajeHabitacion() {
		return menajeHabitacion;
	}

	/**
	 * @param menajeHabitacion the menajeHabitacion to set
	 */
	public void setMenajeHabitacion(String menajeHabitacion) {
		this.menajeHabitacion = menajeHabitacion;
	}
	
	

}

package vos;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonProperty;

public class OfertaHabitacion extends Oferta{
	
	@JsonProperty(value = "tipoDeBanio")
	private String tipoDeBanio;
	
	@JsonProperty(value = "tipoDeHabitacion")
	private String tipoDeHabitacion;
	
	
	public OfertaHabitacion(@JsonProperty(value = "idOferta") Long idOferta, @JsonProperty(value = "valor") Double valor, @JsonProperty(value = "duracion") String duracion, @JsonProperty(value = "fechaInicio") Timestamp fechaInicio, 
			@JsonProperty(value = "fechaFinal") Timestamp fechaFinal, @JsonProperty(value = "cantidadDisponible") Integer cantidadDisponible, @JsonProperty(value = "direccion") String direccion, 
			@JsonProperty(value = "tipoDeOferta") String tipoDeOferta, @JsonProperty(value = "cantidadIncial") Integer cantidadInicial, @JsonProperty(value = "tipoDeBanio") String tipoDeBanio, 
			@JsonProperty(value = "tipoDeHabitacion") String tipoDeHabitacion) {
		super(idOferta, valor, duracion, fechaInicio, fechaFinal, cantidadDisponible, direccion, tipoDeOferta, cantidadInicial);
		this.tipoDeBanio = tipoDeBanio;
		this.tipoDeHabitacion = tipoDeHabitacion; 
	}
	
	public OfertaHabitacion() {
		//Empty constructor
	}

	public String getTipoDeBanio() {
		return tipoDeBanio;
	}

	public void setTipoDeBanio(String tipoDeBanio) {
		this.tipoDeBanio = tipoDeBanio;
	}

	public String getTipoDeHabitacion() {
		return tipoDeHabitacion;
	}

	public void setTipoDeHabitacion(String tipoDeHabitacion) {
		this.tipoDeHabitacion = tipoDeHabitacion;
	}
	
	
	

}

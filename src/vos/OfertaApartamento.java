package vos;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonProperty;

public class OfertaApartamento extends Oferta {
	@JsonProperty(value = "estaAmoblado")
	private boolean estaAmoblado;
	
	public OfertaApartamento(@JsonProperty(value = "idOferta") Long idOferta, @JsonProperty(value = "valor") Double valor, @JsonProperty(value = "duracion") String duracion, @JsonProperty(value = "fechaInicio") Timestamp fechaInicio, 
			@JsonProperty(value = "fechaFinal") Timestamp fechaFinal, @JsonProperty(value = "cantidadDisponible") Integer cantidadDisponible, @JsonProperty(value = "direccion") String direccion, 
			@JsonProperty(value = "tipoDeOferta") String tipoDeOferta, @JsonProperty(value = "cantidadIncial") Integer cantidadInicial, @JsonProperty(value = "estaAmoblado") boolean estaAmoblado) {
	super(idOferta, valor, duracion, fechaInicio, fechaFinal, cantidadDisponible, direccion, tipoDeOferta, cantidadInicial);
	this.estaAmoblado = estaAmoblado;	
	}
	
	public OfertaApartamento() {
		//Empty constructor
	}

	public boolean isEstaAmoblado() {
		return estaAmoblado;
	}

	public void setEstaAmoblado(boolean estaAmoblado) {
		this.estaAmoblado = estaAmoblado;
	}
	
	
}

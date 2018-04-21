package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Hotel extends Operador {
	
	@JsonProperty(value = "RUT")
	private Long RUT;

	public Hotel(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
			@JsonProperty(value = "tipoDeOperador") String tipoDeOperador, @JsonProperty(value = "RUT") Long RUT) 
	{
		super(idOperador, nombre, direccion, telefono, tipoDeOperador);
		// TODO Auto-generated constructor stub
		this.RUT = RUT;
	}
	
	
	public Hotel() {
		//Empty Constructor
	}


	public Long getRUT() {
		return RUT;
	}


	public void setRUT(Long rUT) {
		RUT = rUT;
	}
	
}

package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ArrendadorHabitacion extends Operador {
	
	@JsonProperty(value = "value")
	private Long cedula;
	
	public ArrendadorHabitacion(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
			@JsonProperty(value = "tipoDeOperador") String tipoDeOperador, @JsonProperty(value = "cedula") Long cedula) {
		super(idOperador, nombre, direccion, telefono, tipoDeOperador);
		this.cedula = cedula;
	}

	public ArrendadorHabitacion () {
	 //Empty constructor
	}
	
	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}
	
	
	
	

}

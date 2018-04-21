package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class EmpresaViviendaUniversitaria extends Operador {
	
	@JsonProperty(value = "tipoHabitacion")
	private String tipoHabitacion;
	
	@JsonProperty(value = "capacidadPersonas")
	private Integer capacidadPersonas;

	@JsonProperty(value = "ubicacionHabitacion")
	private String ubicacionHabitacion;
	
	@JsonProperty(value = "mensajeHabitacion")
	private String mensajeHabitacion;
	
	public EmpresaViviendaUniversitaria(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
			@JsonProperty(value = "tipoDeOperador") String tipoDeOperador, @JsonProperty(value = "tipoHabitacion")String tipoHabitacion, 
					@JsonProperty("capacidadPersonas")Integer capacidadPersonas, @JsonProperty(value = "ubicacionHabitacion")String ubicacionHabitacion, 
					@JsonProperty("mensajeHabitacion")String mensajeHabitacion) {
		super(idOperador, nombre, direccion, telefono, tipoDeOperador);
		this.tipoHabitacion = tipoHabitacion;
		this.capacidadPersonas = capacidadPersonas;
		this.ubicacionHabitacion = ubicacionHabitacion;
		this.mensajeHabitacion = mensajeHabitacion;
	}
	
	public EmpresaViviendaUniversitaria() {
		//Empty constructor
	}

	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	public Integer getCapacidadPersonas() {
		return capacidadPersonas;
	}

	public void setCapacidadPersonas(Integer capacidadPersonas) {
		this.capacidadPersonas = capacidadPersonas;
	}

	public String getUbicacionHabitacion() {
		return ubicacionHabitacion;
	}

	public void setUbicacionHabitacion(String ubicacionHabitacion) {
		this.ubicacionHabitacion = ubicacionHabitacion;
	}

	public String getMensajeHabitacion() {
		return mensajeHabitacion;
	}

	public void setMensajeHabitacion(String mensajeHabitacion) {
		this.mensajeHabitacion = mensajeHabitacion;
	}
	
	
	
	
	
	

}

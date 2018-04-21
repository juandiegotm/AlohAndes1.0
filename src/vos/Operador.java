package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Operador {
	
	@JsonProperty("idOperador")
	protected Long idOperador;
	
	@JsonProperty("nombre")
	protected String nombre;
	
	@JsonProperty("direccion")
	protected String direccion;
	
	@JsonProperty("telefono")
	protected Long telefono;
	
	@JsonProperty("tipoDeOperador")
	protected String tipoDeOperador;

	public Long getIdOperador() {
		return idOperador;
	}

	
	
	public Operador(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
					@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
					@JsonProperty(value = "tipoDeOperador") String tipoDeOperador) {
		
		this.idOperador = idOperador;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.tipoDeOperador = tipoDeOperador;
	}

	public Operador() {
		//dummy constructor
	}


	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Long getTelefono() {
		return telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public String getTipoDeOperador() {
		return tipoDeOperador;
	}

	public void setTipoDeOperador(String tipoDeOperador) {
		this.tipoDeOperador = tipoDeOperador;
	}
	
	
	
	
	
	
}

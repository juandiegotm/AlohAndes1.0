package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class PersonaHabilitada {

	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------
	
	@JsonProperty(value = "IdPersonaHabilitada")
	private Long IdPersonaHabilitada;
	
	@JsonProperty(value = "nombre")
	private String nombre;
	
	@JsonProperty(value = "cedula")
	private Long cedula;
	
	@JsonProperty(value = "telefono")
	private Long telefono;
	
	@JsonProperty(value = "email")
	private String email;

	
	
	public PersonaHabilitada(@JsonProperty(value = "IdPersonaHabilitada") Long idPersonaHabilitada, @JsonProperty(value = "nombre") String nombre, @JsonProperty(value = "cedula") Long cedula, @JsonProperty(value = "telefono") Long telefono,
							@JsonProperty(value = "correo") String email) {
		this.IdPersonaHabilitada = idPersonaHabilitada;
		this.nombre = nombre;
		this.cedula = cedula;
		this.telefono = telefono;
		this.email = email;
	}
	
	public PersonaHabilitada() {
		//Dummy constructor
	}

	public Long getIdPersonaHabilitada() {
		return IdPersonaHabilitada;
	}

	public void setIdPersonaHabilitada(Long idPersonaHabilitada) {
		IdPersonaHabilitada = idPersonaHabilitada;
	}

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}

	public Long getTelefono() {
		return telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	 
	
}

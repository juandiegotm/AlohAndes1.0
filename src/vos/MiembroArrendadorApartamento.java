package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class MiembroArrendadorApartamento extends Operador {

	@JsonProperty(value = "tipoMiembro")
	private String tipoMiembro;

	@JsonProperty(value = "cedula")
	private Long cedula;

	@JsonProperty(value = "idMiembro")
	private Long idMiembro;

	public MiembroArrendadorApartamento(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
			@JsonProperty(value = "tipoDeOperador") String tipoDeOperador, @JsonProperty(value = "cedula") Long cedula,
			@JsonProperty(value = "tipoMiembro") String tipoMiembro, @JsonProperty(value = "idMiembro") Long idMiembro) 
	{
		super(idOperador, nombre, direccion, telefono, tipoDeOperador);
		this.cedula = cedula;
		this.tipoMiembro = tipoMiembro;
		this.idMiembro = idMiembro;
	}
	
	public MiembroArrendadorApartamento() {
		//Empty contructor
	}

	public String getTipoMiembro() {
		return tipoMiembro;
	}

	public void setTipoMiembro(String tipoMiembro) {
		this.tipoMiembro = tipoMiembro;
	}

	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}

	public Long getIdMiembro() {
		return idMiembro;
	}

	public void setIdMiembro(Long idMiembro) {
		this.idMiembro = idMiembro;
	}
	
	


}

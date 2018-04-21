package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ArrendadorEsporadico extends Operador {
	
	@JsonProperty(value = "cedula")
	private Long cedula;
	
	@JsonProperty(value = "diasArrendados")
	private Integer diasArrendados;
	
	public ArrendadorEsporadico(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
			@JsonProperty(value = "tipoDeOperador") String tipoDeOperador, @JsonProperty(value = "cedula") Long cedula, 
			@JsonProperty(value = "diasArrendados") Integer diasArrendados) {
		super(idOperador, nombre, direccion, telefono, tipoDeOperador);
		this.diasArrendados = diasArrendados;
		this.cedula = cedula;
	}
	
	public ArrendadorEsporadico() {

	}

	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}

	public Integer getDiasArrendados() {
		return diasArrendados;
	}

	public void setDiasArrendados(Integer diasArrendados) {
		this.diasArrendados = diasArrendados;
	}
	
	
	
	
}

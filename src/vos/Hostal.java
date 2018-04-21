package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Hostal extends Operador{

	@JsonProperty(value = "RUT")
	private Long RUT;
	
	@JsonProperty(value = "horarioAperturaHostal")
	private String horarioAperturaHostal;
	
	@JsonProperty(value = "horarioCierreHostal")
	private String horarioCierreHostal;
	
	public Hostal(@JsonProperty(value = "idOperador") Long idOperador, @JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value = "direccion") String direccion, @JsonProperty(value = "telefono") Long telefono, 
			@JsonProperty(value = "tipoDeOperador") String tipoDeOperador,
			@JsonProperty(value = "RUT") Long RUT, @JsonProperty(value = "horarioAperturaHostal") String horarioAperturaHostal, 
			@JsonProperty(value = "horarioCierreHostal") String horarioCierreHostal) {
		super(idOperador, nombre, direccion, telefono, tipoDeOperador);
		this.RUT = RUT;
		this.horarioAperturaHostal = horarioAperturaHostal;
		this.horarioCierreHostal = horarioCierreHostal;
	}
	
	public Hostal() {
		
	}

	public Long getRUT() {
		return RUT;
	}

	public void setRUT(Long rUT) {
		RUT = rUT;
	}

	public String getHorarioAperturaHostal() {
		return horarioAperturaHostal;
	}

	public void setHorarioAperturaHostal(String horarioAperturaHostal) {
		this.horarioAperturaHostal = horarioAperturaHostal;
	}

	public String getHorarioCierreHostal() {
		return horarioCierreHostal;
	}

	public void setHorarioCierreHostal(String horarioCierreHostal) {
		this.horarioCierreHostal = horarioCierreHostal;
	}
	
	
	
	
	
	
}

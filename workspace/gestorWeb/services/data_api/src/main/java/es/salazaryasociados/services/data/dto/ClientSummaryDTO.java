package es.salazaryasociados.services.data.dto;

import lombok.Data;

@Data
public class ClientSummaryDTO {

	private Integer id;
	private String apellidos;
	private String correoElectronico;
	private String direccion;
	private String nombre;
	private String observaciones;
	private String poblacion;
	private String telefono;
	private String telefono2;
	private String dni;
}

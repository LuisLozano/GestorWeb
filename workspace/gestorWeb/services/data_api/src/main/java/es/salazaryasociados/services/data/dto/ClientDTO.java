package es.salazaryasociados.services.data.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class ClientDTO {
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
	
	private Set<FileSummaryDTO> files = new HashSet<FileSummaryDTO>();
	private Set<DocumentSummaryDTO> documents = new HashSet<DocumentSummaryDTO>();
}

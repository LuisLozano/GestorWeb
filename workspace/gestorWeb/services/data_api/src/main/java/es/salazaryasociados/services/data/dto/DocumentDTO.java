package es.salazaryasociados.services.data.dto;

import lombok.Data;

@Data
public class DocumentDTO {

	private Integer id;
	private String descripcion;
	private String ruta;	
	private FileSummaryDTO file;
	private ClientSummaryDTO client;
}

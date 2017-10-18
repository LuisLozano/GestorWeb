package es.salazaryasociados.services.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class EventDTO {

	private Integer id;
	private Timestamp fechaInicio;
	private Timestamp fechaFin;
	private String observaciones;
	private String titulo;
	private Boolean enviarMailResp = new Boolean(false);
	private Boolean enviarMailClientes = new Boolean(false);	
	private FileSummaryDTO file;	
}

package es.salazaryasociados.services.data.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class FileDTO {

	private Integer id;
	private String asunto;
	private Boolean cerrado = new Boolean(false);
	private String observaciones;
	private Date fechaApertura;
	private Date fechaCierre;		
	private BigDecimal presupuesto = new BigDecimal(0.0);
	private Set<PaymentSummaryDTO> payments = new HashSet<PaymentSummaryDTO>();
	private Set<EventDTO> events = new HashSet<EventDTO>();
	private Set<DocumentSummaryDTO> documents = new HashSet<DocumentSummaryDTO>();	
	private Set<ClientSummaryDTO> clients = new HashSet<ClientSummaryDTO>();
	private ResponsibleDTO responsible1;
	private ResponsibleDTO responsible2;
}

package es.salazaryasociados.services.data.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class PaymentDTO {

	private Integer id;
	private BigDecimal cantidad = new BigDecimal(0.0);
	private Timestamp fecha;
	private String observaciones;
	private ClientSummaryDTO payer;
	private FileSummaryDTO file;	
}

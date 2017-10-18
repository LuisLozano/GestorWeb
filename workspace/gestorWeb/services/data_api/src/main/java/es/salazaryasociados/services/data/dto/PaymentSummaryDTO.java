package es.salazaryasociados.services.data.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class PaymentSummaryDTO {

	private Integer id;
	private BigDecimal cantidad = new BigDecimal(0.0);
	private Timestamp fecha;
	private String observaciones;
	private String payer;
	private Integer file;		
}

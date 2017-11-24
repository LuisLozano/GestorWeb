package es.salazaryasociados.services.data.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class FileSummaryDTO {

	private Integer id;
	private String asunto;
	private Boolean cerrado = new Boolean(false);
	private String observaciones;
	private BigDecimal presupuesto = new BigDecimal(0.0);
	private BigDecimal pagado = new BigDecimal(0.0);
	private String responsable;	
	private BigDecimal moroso = new BigDecimal(0.0);
	private Date fechaApertura;
	private Date fechaCierre;			
}

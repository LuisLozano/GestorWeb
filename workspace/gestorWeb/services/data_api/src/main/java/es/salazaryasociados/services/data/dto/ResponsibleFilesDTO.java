package es.salazaryasociados.services.data.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ResponsibleFilesDTO {

	private String apellidos;
	private String nombre;
	private Integer expId;
	private ResponsibleDTO responsible1;
	private ResponsibleDTO responsible2;
	private String asunto;
	private Boolean cerrado = new Boolean(false);
	private BigDecimal presupuesto = new BigDecimal(0.0);
	private BigDecimal pagado = new BigDecimal(0.0);	
	private String telefono;
	private Integer clientId;
	private String observaciones;	
}

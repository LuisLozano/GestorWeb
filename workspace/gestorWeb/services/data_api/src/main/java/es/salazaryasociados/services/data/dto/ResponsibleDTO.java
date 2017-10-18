package es.salazaryasociados.services.data.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponsibleDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3279487121609197722L;
	private Integer id;
	private String nombre;
	private String email;
}

package es.salazaryasociados.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name="Evento")
@Data
public class Evento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2738947054690852394L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;
	
	@NotNull(message="El evento debe tener una fecha de inicio")
	@Column(name="FechaInicio", nullable = false)
	private Timestamp fechaInicio;
	
	@NotNull(message="El evento debe tener una fecha de fin")
	@Column(name="FechaFin", nullable = false)
	private Timestamp fechaFin;
	
	@NotNull(message="El evento debe tener un título")
	@Column(name="Titulo")
	private String titulo;
	
	@Column(name="Observaciones")
	private String observaciones;
	
	@Column(name="EnviarMailResponsable")
	private Boolean enviarMailResp = new Boolean(false);
	
	@Column(name="EnviarMailClientes")
	private Boolean enviarMailClientes = new Boolean(false);	
	
	//bi-directional many-to-one association to Responsable
	@ManyToOne
	@JoinColumn(name="Expediente", nullable = false)
	private Expediente expediente;	
}

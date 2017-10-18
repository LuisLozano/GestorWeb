package es.salazaryasociados.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The persistent class for the "Expediente" database table.
 * 
 */
@Entity
@Table(name="Expediente")
@NamedQuery(name="Expediente.findAll", query="SELECT e FROM Expediente e")
@NamedEntityGraph(name = "graph.expediente.list_all", 
	attributeNodes = @NamedAttributeNode("pagos"))
public class Expediente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Id")
	private Integer id;

	@NotNull(message="El expediente debe tener un asunto")
	@Size(max=10000, min=4,message="El asunto debe tener más de 4 caracteres y menos de 10000")
	@Column(name="Asunto", nullable = false)
	private String asunto;

	@Column(name="Cerrado", nullable = false)
	private Boolean cerrado = new Boolean(false);

	@Column(name="Observaciones")
	private String observaciones;

	@Column(name="Presupuesto")
	private BigDecimal presupuesto = new BigDecimal(0.0);

	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="expediente", orphanRemoval=true)
	private Set<Pago> pagos = new HashSet<Pago>();
	
	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="expediente", orphanRemoval=true)
	private Set<Evento> eventos = new HashSet<Evento>();	
	
	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="expediente", orphanRemoval=true)
	private Set<Documento> documentos = new HashSet<Documento>();	

	//bi-directional many-to-many association to Cliente
	@ManyToMany
	@JoinTable(
		name="ExpedienteVSCliente"
		, joinColumns={@JoinColumn(name="expediente", referencedColumnName="id")}
		, inverseJoinColumns={@JoinColumn(name="cliente", referencedColumnName="id")}
		)
	private Set<Cliente> clientes = new HashSet<Cliente>();

	//bi-directional many-to-one association to Responsable
	@ManyToOne
	@JoinColumn(name="responsable1", nullable = false)
	private Responsable responsable1;

	//bi-directional many-to-one association to Responsable
	@ManyToOne
	@JoinColumn(name="responsable2")
	private Responsable responsable2;

	public Expediente() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public Boolean getCerrado() {
		return this.cerrado;
	}

	public void setCerrado(Boolean cerrado) {
		this.cerrado = cerrado;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BigDecimal getPresupuesto() {
		return this.presupuesto;
	}

	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}

	public Set<Documento> getDocumentos() {
		return this.documentos;
	}

	public Set<Cliente> getClientes() {
		return this.clientes;
	}

	public Responsable getResponsable1() {
		return this.responsable1;
	}

	public void setResponsable1(Responsable r1) {
		this.responsable1 = r1;
	}

	public Responsable getResponsable2() {
		return this.responsable2;
	}

	public void setResponsable2(Responsable r2) {
		this.responsable2 = r2;
	}

	public Set<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(Set<Pago> pagos) {
		this.pagos = pagos;
	}

	public void setDocumentos(Set<Documento> documentos) {
		this.documentos = documentos;
	}

	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Set<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(Set<Evento> eventos) {
		this.eventos = eventos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asunto == null) ? 0 : asunto.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expediente other = (Expediente) obj;
		if (asunto == null) {
			if (other.asunto != null)
				return false;
		} else if (!asunto.equals(other.asunto))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
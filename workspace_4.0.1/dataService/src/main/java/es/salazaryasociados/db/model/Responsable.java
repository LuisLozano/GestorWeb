package es.salazaryasociados.db.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="Responsable", 
		uniqueConstraints={
		   @UniqueConstraint(columnNames={"nombre"})
		})
@NamedQuery(name="Responsable.findAll", query="SELECT r FROM Responsable r")
public class Responsable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;

	@NotNull(message="RESPONSABLE_NULL_NAME")
	@Size(max=20, min=4,message="RESPONSABLE_SHORT_NAME")
	@Column(name="Nombre", nullable = false)
	private String nombre;

	//bi-directional many-to-one association to Expediente
	@OneToMany(mappedBy="responsable1")
	private Set<Expediente> expedientes1 = new HashSet<Expediente>();

	//bi-directional many-to-one association to Expediente
	@OneToMany(mappedBy="responsable2")
	private Set<Expediente> expedientes2 = new HashSet<Expediente>();

	public Responsable() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Expediente> getExpedientes1() {
		return this.expedientes1;
	}

	public Set<Expediente> getExpedientes2() {
		return this.expedientes2;
	}

	public void setExpedientes1(Set<Expediente> expedientes1) {
		this.expedientes1 = expedientes1;
	}

	public void setExpedientes2(Set<Expediente> expedientes2) {
		this.expedientes2 = expedientes2;
	}
}
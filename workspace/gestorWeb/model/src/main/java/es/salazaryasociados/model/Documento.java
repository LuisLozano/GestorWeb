package es.salazaryasociados.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Documento")
@NamedQuery(name="Documento.findAll", query="SELECT d FROM Documento d")
public class Documento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;

	@Column(name="Descripcion")
	private String descripcion;

	@NotNull(message="Debe definir el directorio para guardar el documento")
	@Column(name="Ruta", nullable = false)
	private String ruta;

	//bi-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="expediente", nullable = true)
	private Expediente expediente;

	//bi-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="cliente", nullable = true)
	private Cliente cliente;
	
	public Documento() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente exp) {
		this.expediente = exp;
	}	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ruta == null) ? 0 : ruta.hashCode());
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
		Documento other = (Documento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ruta == null) {
			if (other.ruta != null)
				return false;
		} else if (!ruta.equals(other.ruta))
			return false;
		return true;
	}
}
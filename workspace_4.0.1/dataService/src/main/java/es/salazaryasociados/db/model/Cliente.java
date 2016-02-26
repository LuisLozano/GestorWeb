package es.salazaryasociados.db.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The persistent class for the "Cliente" database table.
 * 
 */
@Entity
@Table(name="Cliente", schema = "public", 
		uniqueConstraints={
		   @UniqueConstraint(columnNames={"nombre", "apellidos"})
		})
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="CLIENTE_ID_GENERATOR", sequenceName="CLIENTE_ID_SEQ")
	//@GeneratedValue(strategy=GenerationType.AUTO, generator="CLIENTE_ID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;

	@NotNull(message="CLIENT_NULL_SURNAME")
	@Size(max=100, min=4,message="CLIENT_SHORT_SURNAME")
	@Column(name="Apellidos", nullable = false)
	private String apellidos;

	@Column(name="CorreoElectronico")
	private String correoElectronico;

	@Column(name="Direccion")
	private String direccion;

	@NotNull(message="CLIENT_NULL_NAME")
	@Size(max=50, min=4,message="CLIENT_SHORT_NAME")
	@Column(name="Nombre", nullable = false)
	private String nombre;

	@Column(name="Observaciones")
	private String observaciones;

	@Column(name="Poblacion")
	private String poblacion;

	@Column(name="Telefono")
	private String telefono;

	@Column(name="Telefono2")
	private String telefono2;

	//bi-directional many-to-many association to Expediente
	@ManyToMany(mappedBy="clientes")
	private Set<Expediente> expedientes = new HashSet<Expediente>();
	
	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="pagador")
	private Set<Pago> pagos = new HashSet<Pago>();	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Cliente other = (Cliente) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	public Cliente() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreoElectronico() {
		return this.correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public Set<Expediente> getExpedientes() {
		return this.expedientes;
	}

	public void setExpedientes(Set<Expediente> expedientes) {
		this.expedientes = expedientes;
	}

	public Set<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(Set<Pago> pagos) {
		this.pagos = pagos;
	}	
}
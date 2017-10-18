package es.salazaryasociados.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="LISTADO_EXP_PARA_RESP")
public class ListadoExpResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4623761720143993096L;

	@Column(name="Apellidos", nullable = false)
	private String apellidos;
	
	@Column(name="Nombre", nullable = false)
	private String nombre;
	
	@Id
	@Column(name="ExpId")
	private Integer expId;
	
	//bi-directional many-to-one association to Responsable
	@ManyToOne
	@JoinColumn(name="responsable1", nullable = false)
	private Responsable responsable1;

	//bi-directional many-to-one association to Responsable
	@ManyToOne
	@JoinColumn(name="responsable2")
	private Responsable responsable2;
	
	@Column(name="Asunto", nullable = false)
	private String asunto;

	@Column(name="Cerrado", nullable = false)
	private Boolean cerrado = new Boolean(false);
	
	@Column(name="Presupuesto")
	private BigDecimal presupuesto = new BigDecimal(0.0);
	
	@Column(name="Telefono")
	private String telefono;
	
	@Column(name="ClientId")
	private Integer clientId;
	
	@Column(name="Observaciones")
	private String observaciones;

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getExpId() {
		return expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public Responsable getResponsable1() {
		return responsable1;
	}

	public void setResponsable1(Responsable responsable1) {
		this.responsable1 = responsable1;
	}

	public Responsable getResponsable2() {
		return responsable2;
	}

	public void setResponsable2(Responsable responsable2) {
		this.responsable2 = responsable2;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public Boolean getCerrado() {
		return cerrado;
	}

	public void setCerrado(Boolean cerrado) {
		this.cerrado = cerrado;
	}

	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((asunto == null) ? 0 : asunto.hashCode());
		result = prime * result + ((cerrado == null) ? 0 : cerrado.hashCode());
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((expId == null) ? 0 : expId.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((observaciones == null) ? 0 : observaciones.hashCode());
		result = prime * result
				+ ((presupuesto == null) ? 0 : presupuesto.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
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
		ListadoExpResp other = (ListadoExpResp) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (asunto == null) {
			if (other.asunto != null)
				return false;
		} else if (!asunto.equals(other.asunto))
			return false;
		if (cerrado == null) {
			if (other.cerrado != null)
				return false;
		} else if (!cerrado.equals(other.cerrado))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (expId == null) {
			if (other.expId != null)
				return false;
		} else if (!expId.equals(other.expId))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (observaciones == null) {
			if (other.observaciones != null)
				return false;
		} else if (!observaciones.equals(other.observaciones))
			return false;
		if (presupuesto == null) {
			if (other.presupuesto != null)
				return false;
		} else if (!presupuesto.equals(other.presupuesto))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		return true;
	}	
}

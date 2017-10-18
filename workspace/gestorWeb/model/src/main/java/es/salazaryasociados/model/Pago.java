package es.salazaryasociados.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Pago")
@NamedQuery(name="Pago.findAll", query="SELECT p FROM Pago p")
public class Pago implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;

	@DecimalMin(value="0.0001",message="La cantidad mínima para un pago es de 0.0001 euros")
	@Column(name="Cantidad", nullable = false)
	private BigDecimal cantidad = new BigDecimal(0.0);

	@NotNull(message="Debe definir una fecha de pago")
	@Column(name="Fecha", nullable = false)
	private Timestamp fecha;

	@Column(name="Observaciones")
	private String observaciones;

	@ManyToOne
	@JoinColumn(name="Pagador", nullable = false)
	private Cliente pagador;
	
	//bi-directional many-to-one association to Responsable
	@ManyToOne
	@JoinColumn(name="Expediente", nullable = false)
	private Expediente expediente;	

	public Pago() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Cliente getPagador() {
		return this.pagador;
	}

	public void setPagador(Cliente pg) {
		this.pagador = pg;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente exp) {
		this.expediente = exp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cantidad == null) ? 0 : cantidad.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
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
		Pago other = (Pago) obj;
		if (cantidad == null) {
			if (other.cantidad != null)
				return false;
		} else if (!cantidad.equals(other.cantidad))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
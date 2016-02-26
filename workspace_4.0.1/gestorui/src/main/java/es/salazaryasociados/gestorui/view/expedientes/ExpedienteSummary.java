package es.salazaryasociados.gestorui.view.expedientes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.Pago;

public class ExpedienteSummary implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String asunto = "";
	private String responsable = "";
	private BigDecimal presupuesto = new BigDecimal(0);
	private BigDecimal pagado = new BigDecimal(0);
	private BigDecimal moroso = new BigDecimal(0);
	private Boolean closed = new Boolean(false);
	private String obs = "";
	
	public ExpedienteSummary(final Expediente expediente)
	{
		if (expediente != null)
		{
			id = expediente.getId().longValue();
			asunto = expediente.getAsunto();
			responsable = expediente.getResponsable1().getNombre();
			presupuesto = expediente.getPresupuesto();
			presupuesto.setScale(2, RoundingMode.CEILING);
			pagado = calculatePayment(expediente);
			moroso = new BigDecimal(presupuesto.floatValue() - pagado.floatValue());
			moroso.setScale(2, RoundingMode.CEILING);
			closed = expediente.getCerrado();
			obs = expediente.getObservaciones();
		}		
	}
	
	private BigDecimal calculatePayment(Expediente expediente) {
		BigDecimal pagos = new BigDecimal(0.0);
		double aux = 0.0;
		if (expediente != null && expediente.getPagos() != null)
		{
			for (Pago p : expediente.getPagos())
			{
				aux += p.getCantidad().doubleValue();
			}
			
			pagos = new BigDecimal(aux);
		}
		
		pagos.setScale(2, RoundingMode.CEILING);
		return pagos;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public BigDecimal getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}
	public BigDecimal getPagado() {
		return pagado;
	}
	public void setPagado(BigDecimal pagado) {
		this.pagado = pagado;
	}
	public BigDecimal getMoroso() {
		return moroso;
	}
	public void setMoroso(BigDecimal moroso) {
		this.moroso = moroso;
	}
	public Boolean getClosed() {
		return closed;
	}
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
}

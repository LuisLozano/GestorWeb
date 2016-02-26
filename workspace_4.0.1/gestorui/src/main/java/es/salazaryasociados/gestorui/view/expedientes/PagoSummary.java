package es.salazaryasociados.gestorui.view.expedientes;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Pago;
import es.salazaryasociados.db.service.IDataService;

public class PagoSummary {

	private Timestamp fecha;
	private String pagador;
	private BigDecimal cantidad;
	private String observaciones;
	private Integer id;
	
	public PagoSummary(Pago pago) {
		pagador = pagador(pago.getId());
		fecha = pago.getFecha();
		cantidad = pago.getCantidad();
		observaciones = pago.getObservaciones();
		id = pago.getId();
	}

	private String pagador(Integer p){
		IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		String result = "";
		try {
			Pago pago = dataService.getPagoById(p, "pagador");
			
			String nombre = pago.getPagador().getNombre();
			if (nombre == null){
				nombre = "";
			}
			
			String apellidos = pago.getPagador().getApellidos();
			if (apellidos == null){
				apellidos = "";
			}
			result = nombre + " " + apellidos;			
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
	        result = "ERROR";
		}
		return result;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getPagador() {
		return pagador;
	}

	public void setPagador(String pagador) {
		this.pagador = pagador;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
}

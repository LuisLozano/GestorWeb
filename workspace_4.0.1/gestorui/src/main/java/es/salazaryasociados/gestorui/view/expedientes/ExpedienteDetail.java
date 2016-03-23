package es.salazaryasociados.gestorui.view.expedientes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.Pago;
import es.salazaryasociados.db.service.IDataService;
import es.salazaryasociados.gestorui.view.clientes.ClientesDataModel;

@ManagedBean
@ViewScoped
public class ExpedienteDetail {

	@ManagedProperty(value="#{expedientesBean}")
	private ExpedientesBean expedientesBean;
	
	private IDataService dataService;
	
	private Expediente theFile;
	
	private BigDecimal pagado = new BigDecimal(0.0);
	private BigDecimal moroso = new BigDecimal(0.0);
	private List<PagoSummary> pagos = new ArrayList<PagoSummary>();
	private ClientesDataModel clientesModel;
	private Integer responsable1;
	private Integer responsable2;
	private Integer pagadorID;

	private Pago newPago = new Pago();

	
	@PostConstruct
    public void init() {
		dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		clientesModel = new ClientesDataModel();
		if (theFile == null)
		{
			if (expedientesBean.getSelectedFile() != null) {
				try {
					theFile = dataService.getFileById(expedientesBean.getSelectedFile().intValue(), "pagos", "clientes");

					for (Pago pago : theFile.getPagos()){
						pagado = pagado.add(pago.getCantidad());
						pagos.add(new PagoSummary(pago));
					}
					
					if (theFile.getPresupuesto().compareTo(pagado) < 0){
						theFile.setPresupuesto(pagado);
					}					
					moroso = theFile.getPresupuesto().subtract(pagado);			
										
					responsable1 = theFile.getResponsable1() != null ? theFile.getResponsable1().getId() : -1;
					responsable2 = theFile.getResponsable2() != null ? theFile.getResponsable2().getId() : -1;
				} catch (DataException e) {
					FacesContext context = FacesContext.getCurrentInstance();	         
			        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
				}
			}
			else
			{
				theFile = new Expediente();
			}
		}
    }	
	
	public Expediente getTheFile() {
		return theFile;
	}

	public void setTheFile(Expediente theFile) {
		this.theFile = theFile;
	}

	public ExpedientesBean getExpedientesBean() {
		return expedientesBean;
	}

	public void setExpedientesBean(ExpedientesBean expedientesBean) {
		this.expedientesBean = expedientesBean;
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

	public List<PagoSummary> getPagos() {
		return pagos;
	}

	public void setPagos(List<PagoSummary> pagos) {
		this.pagos = pagos;
	}
	
	public ClientesDataModel getClientesModel() {
		return clientesModel;
	}

	public void setClientesModel(ClientesDataModel clientesModel) {
		this.clientesModel = clientesModel;
	}

	public Integer getResponsable1() {
		return responsable1;
	}

	public void setResponsable1(Integer responsable1) {
		this.responsable1 = responsable1;
	}

	public Integer getResponsable2() {
		return responsable2;
	}

	public void setResponsable2(Integer responsable2) {
		this.responsable2 = responsable2;
	}

	public Pago getNewPago() {
		return newPago;
	}

	public void setNewPago(Pago newPago) {
		this.newPago = newPago;
	}

	public Integer getPagadorID() {
		return pagadorID;
	}

	public void setPagadorID(Integer pagodorID) {
		this.pagadorID = pagodorID;
	}

	public void addClient(Integer clienteID){
		try {		
			theFile.getClientes().add(dataService.getClientById(clienteID));
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}			
	}
	
	public void deleteClient(Integer clientID){
		try {		
			
			// Es más rápido buscar en la lista de clientes:
			Cliente c = null;
			for (Cliente client : theFile.getClientes()){
				if (client.getId().intValue() == clientID.intValue()){
					c = client;
					break;
				}
			}
			
			if (c != null)
			{
				theFile.getClientes().remove(c);
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}					
	}
	
	public void addPago(){
		
		try {
			
			if (theFile.getId() == null){
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  "El expediente debe tener un número asignado antes de aceptar pagos. Pulse Guardar") );				
				return;
			}
			
			newPago.setPagador(dataService.getClientById(pagadorID.intValue()));
			newPago.setFecha(new Timestamp((new Date()).getTime()));
			dataService.addPagoToExpediente(theFile.getId(), newPago);
			pagos.add(new PagoSummary(newPago));			
			pagado = pagado.add(newPago.getCantidad());
			if (theFile.getPresupuesto().compareTo(pagado) < 0){
				theFile.setPresupuesto(pagado);
				dataService.saveExpediente(theFile);
			}					
			moroso = theFile.getPresupuesto().subtract(pagado);			
			
			newPago = new Pago();			
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}					
				
	}
	
	public void deletePago(Integer pagoID){
		
		Integer pID = -1;
		int index = -1;
		for(PagoSummary pago : pagos){
			index++;
			if (pago.getId().intValue() == pagoID.intValue()){
				pID = pago.getId();
				break;
			}
		}
		
		if (pID >= 0 && index >= 0 && index < pagos.size()){
			try {
				dataService.removePagoFromExpediente(theFile.getId(), pID);
				theFile = dataService.getFileById(expedientesBean.getSelectedFile().intValue(), "pagos", "clientes");
				pagado = pagado.subtract(pagos.get(index).getCantidad());
				pagos.remove(index);				
				moroso = theFile.getPresupuesto().subtract(pagado);
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
		}
		newPago = new Pago();
	}
	
	public void save(){						
		try {
			
			if (theFile.getResponsable1() == null || responsable1.intValue() != theFile.getResponsable1().getId().intValue()){
				theFile.setResponsable1(dataService.getResponsableById(responsable1));
			}
			
			if (responsable2.intValue() == -1){
				theFile.setResponsable2(null);
			}
			else if (theFile.getResponsable2() != null){
				if (responsable2.intValue() != theFile.getResponsable2().getId().intValue()){
					theFile.setResponsable2(dataService.getResponsableById(responsable2));
				}							
			}
			else{
				theFile.setResponsable2(dataService.getResponsableById(responsable2));
			}
			
			dataService.saveExpediente(theFile);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
	}
	
	public void changePresupuesto() {
		moroso = theFile.getPresupuesto().subtract(pagado);
	}
}

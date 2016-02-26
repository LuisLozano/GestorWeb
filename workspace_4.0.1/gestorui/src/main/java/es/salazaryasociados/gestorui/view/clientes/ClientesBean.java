package es.salazaryasociados.gestorui.view.clientes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.service.IDataService;

@ManagedBean
@ViewScoped
public class ClientesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8116731243500792127L;
	private IDataService dataService;
	private ClientesDataModel clientesModel;
	
	private Integer selectedClientID;
	private Cliente selectedClient;
	private Set<Expediente> filteredFiles;
	
	private boolean viewClosed = false;
	
	@PostConstruct
    public void init() {
		dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		clientesModel = new ClientesDataModel();
    }
	public IDataService getDataService() {
		return dataService;
	}
	public void setDataService(IDataService dataService) {
		this.dataService = dataService;
	}
	public ClientesDataModel getClientesModel() {
		return clientesModel;
	}
	public void setClientesModel(ClientesDataModel clientesModel) {
		this.clientesModel = clientesModel;
	}
	
	public void deleteCliente(){
		try {		
			dataService.deleteCliente(selectedClient.getId());
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}			
	}
	
	public void newCliente(ActionEvent actionEvent){
		selectedClient = new Cliente();
		viewClosed = false;
		filteredFiles = getFiles(null, false);
	}
	public Integer getSelectedClientID() {
		return selectedClientID;
	}
	public void setSelectedClientID(Integer sc) {
		this.selectedClientID = sc;
		try {
			selectedClient = dataService.getClientById(selectedClientID, "expedientes", "pagos");
			viewClosed = false;
			filteredFiles = getFiles(selectedClient.getExpedientes(), viewClosed);			
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
	}
	public Cliente getSelectedClient() {
		return selectedClient;
	}
	public Set<Expediente> getFilteredFiles() {
		return filteredFiles;
	}
	public void setFilteredFiles(Set<Expediente> filteredFiles) {
		this.filteredFiles = filteredFiles;
	}
	public boolean isViewClosed() {
		return viewClosed;
	}
	public void setViewClosed(boolean viewClosed) {
		this.viewClosed = viewClosed;
		if (selectedClient != null)
		{
			filteredFiles = getFiles(selectedClient.getExpedientes(), viewClosed);
		}
	}
	private Set<Expediente> getFiles(Set<Expediente> expedientes,
			boolean closed) {
		
		Set<Expediente> result = new HashSet<Expediente>();
		
		if (expedientes != null) {
			for (Expediente exp : expedientes) {
				if (exp.getCerrado() && closed) {
					result.add(exp);
				}
			}
		}
		
		return result;
	}
	
	public void save() {
		try {
			dataService.saveCliente(selectedClient);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}		
	}
}

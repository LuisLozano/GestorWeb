package es.salazaryasociados.gestorui.view.clients;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import es.salazaryasociados.services.data.exceptions.GestorErrors;
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class ClientsListView extends SalazarBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4433769402684711218L;
	
	@Inject
	protected IDataService dataService;
	@Getter
	@Setter
	private LazyDataModel<ClientSummaryDTO> dataModel;
	@Getter
	@Setter
	private Integer selectedClient;
	
	@PostConstruct
	public void init() {
		super.init();
		dataModel = new ClientsDataModel(dataService);
	}
	
	public void newClient() {
		selectedClient = null;
	}
	
	public void deleteClient() {
		
		if (selectedClient != null) {
			try {
				
				ClientDTO client = dataService.getClientById(selectedClient);
				if (client != null && client.getFiles() != null && client.getFiles().size() > 0) {
					throw new DataServiceException(GestorErrors.CLIENT_HAS_FILES, "Antes debe eliminar los expedientes del cliente");
				}
				dataService.deleteClient(selectedClient);
			} catch (DataServiceException e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();				
			}
		}
		
		selectedClient = null;
	}	
}



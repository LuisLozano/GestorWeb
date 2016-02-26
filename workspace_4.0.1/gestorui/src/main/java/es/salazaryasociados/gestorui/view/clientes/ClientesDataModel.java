package es.salazaryasociados.gestorui.view.clientes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.service.IDataService;

public class ClientesDataModel extends LazyDataModel<Cliente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6142222984071194347L;

	public ClientesDataModel(){
	}
	
	@Override
	public Cliente getRowData(String rowKey) {
		Cliente result = null;
		IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		if (dataService != null) {
			try {
				int lrow = Integer.parseInt(rowKey);
				
				result = dataService.getClientById(lrow);
			} catch (NumberFormatException n) {
				result = null;
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
				result = null;
			}
		}
		return result;
    }	
	
	@Override
    public Object getRowKey(Cliente client) {
        return new Long(client.getId());
    }	
	
	 @Override
	    public List<Cliente> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
	        List<Cliente> data = new ArrayList<Cliente>();
	
	        IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
	        if (dataService == null)
	        {
	        	return data;
	        }
	        
	        //filter & sort
	        try {	        	
				data = dataService.getAllClients(pageSize, first, filters, sortField, SortOrder.DESCENDING.equals(sortOrder));
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
	 	 
	        //rowCount
	        int dataSize = 0;
			try {
				dataSize = (int)dataService.getClientsCount(filters);
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
	        this.setRowCount(dataSize);
	
	        return data;
	    }	
}

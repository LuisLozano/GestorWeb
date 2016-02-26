package es.salazaryasociados.gestorui.view.responsables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.service.IDataService;

public class ResponsablesDataModel  extends LazyDataModel<Responsable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3447175045504751283L;

	@Override
	public Responsable getRowData(String rowKey) {
		Responsable result = null;
		IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		if (dataService != null) {
			try {
				int lrow = Integer.parseInt(rowKey);
				
				result = dataService.getResponsableById(lrow);
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
    public Object getRowKey(Responsable client) {
        return new Long(client.getId());
    }	
	
	 @Override
	    public List<Responsable> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
	        List<Responsable> data = new ArrayList<Responsable>();
	
	        IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
	        if (dataService == null)
	        {
	        	return data;
	        }
	        
	        //filter & sort
	        try {	        	
				data = dataService.getAllResponsables(pageSize, first, filters, sortField, SortOrder.DESCENDING.equals(sortOrder));
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
	 	 
	        //rowCount
	        int dataSize = 0;
			try {
				dataSize = (int)dataService.getResponsablesCount(filters);
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
	        this.setRowCount(dataSize);
	
	        return data;
	    }		
}

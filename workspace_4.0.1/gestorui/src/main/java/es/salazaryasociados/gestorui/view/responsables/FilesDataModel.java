package es.salazaryasociados.gestorui.view.responsables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.ListadoExpResp;
import es.salazaryasociados.db.service.IDataService;

public class FilesDataModel extends LazyDataModel<ListadoExpResp> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8895997407045037809L;
	
	private ResponsablesBean responsablesBean;
	
	public FilesDataModel(ResponsablesBean r) {
		super();
		responsablesBean = r;
	}
	
    public List<ListadoExpResp> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	
    	List<ListadoExpResp> data = new ArrayList<ListadoExpResp>();
    	   	
        IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
        if (dataService == null || responsablesBean == null || responsablesBean.getSelectedResponsable() == null)
        {        	
			return data;
        }
        
        //filter & sort
        // Siempre se filtra por responsable
        Map<String,Object> filtersAux = new HashMap<String, Object>(filters);
        filtersAux.put("responsable1", responsablesBean.getSelectedResponsable());
        
        Object idValue = filtersAux.get("expId");
        if (idValue != null && idValue instanceof String && !((String)idValue).contains("%")){
        	try{
        		Integer id = new Integer((String)idValue);	        		
        		filtersAux.put("expId", id);
        	}catch(Exception e)
        	{
        		filtersAux.remove("expId");
        	}	        	
        }        
        
        if (!responsablesBean.getIncludeClosed()) {
        	filtersAux.put("cerrado", new Boolean(false));
        }
        
        if (responsablesBean.getIncludeSecondResp()) {
        	filtersAux.put("responsable2", responsablesBean.getSelectedResponsable());
        	filtersAux.remove("responsable1");
        }
        
        try {	        	
			data = dataService.getAllExpResp(pageSize, first, filtersAux, sortField, SortOrder.DESCENDING.equals(sortOrder));
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
 	 
        //rowCount
        int dataSize = 0;
		try {
			dataSize = (int)dataService.getAllExpRespCount(filtersAux);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
        this.setRowCount(dataSize);

        return data;
    }
}

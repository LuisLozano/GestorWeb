package es.salazaryasociados.gestorui.view.expedientes;

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
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.service.IDataService;

public class ExpedientesDataModel extends LazyDataModel<ExpedienteSummary> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8148162891895509256L;

	public ExpedientesDataModel(){
	}
	
	private List<ExpedienteSummary> buildExpedientes(List<Expediente> allFiles, IDataService dataService) {

		List<ExpedienteSummary> expedientes = new ArrayList<ExpedienteSummary>();
		if (dataService != null)
		{
			for (Expediente e : allFiles)
			{
				Expediente completeE;
				try {
					completeE = dataService.getFileById(e.getId(), "pagos", "clientes");
					expedientes.add(new ExpedienteSummary(completeE));
				} catch (DataException exception) {
					FacesContext context = FacesContext.getCurrentInstance();	         
			        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  exception.getMessage()) );
				} 				
			}
			
		}		
		return expedientes;
	}
		
	@Override
	public ExpedienteSummary getRowData(String rowKey) {
		ExpedienteSummary result = null;
		IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		if (dataService != null) {
			try {
				int lrow = Integer.parseInt(rowKey);
				
				result = new ExpedienteSummary(dataService.getFileById(lrow));
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
    public Object getRowKey(ExpedienteSummary exp) {
        return new Long(exp.getId());
    }	
	
	 @Override
	    public List<ExpedienteSummary> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
	        List<ExpedienteSummary> data = new ArrayList<ExpedienteSummary>();
	
	        IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
	        if (dataService == null)
	        {
	        	return data;
	        }
	        
	        Map<String,Object> filtersAux = new HashMap<String, Object>(filters);
	        Object respValue = filtersAux.get("responsable1");
	        if (respValue != null && respValue instanceof String)
	        {
	        	try{
	        		int id = Integer.parseInt((String) filtersAux.get("responsable1"));
	        		Responsable resp = dataService.getResponsableById(id);
	        		filtersAux.put("responsable1", resp);
	        	}catch(Exception e)
	        	{
	        		filtersAux.remove("responsable1");
	        	}	        	
	        }
	        
	        Object idValue = filtersAux.get("id");
	        if (idValue != null && idValue instanceof String && !((String)idValue).contains("%")){
	        	try{
	        		Integer id = new Integer((String)idValue);	        		
	        		filtersAux.put("id", id);
	        	}catch(Exception e)
	        	{
	        		filtersAux.remove("id");
	        	}	        	
	        }
	        
	        //filter & sort
	        try {
	        	List<Expediente> lst = dataService.getAllFiles(pageSize, first, filtersAux, sortField, SortOrder.DESCENDING.equals(sortOrder));
				data = buildExpedientes(lst, dataService);
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
	 	 
	        //rowCount
	        int dataSize = 0;
			try {
				dataSize = (int)dataService.getFilesCount(filtersAux);
			} catch (DataException e) {
				FacesContext context = FacesContext.getCurrentInstance();	         
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
			}
	        this.setRowCount(dataSize);
	
	        return data;
	    }	
}

package es.salazaryasociados.gestorui.view.responsables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.Pago;
import es.salazaryasociados.db.service.IDataService;

public class FilesDataModel extends LazyDataModel<ResponsableFile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8895997407045037809L;
	
	private ResponsablesBean responsablesBean;
	
	public FilesDataModel(ResponsablesBean r) {
		super();
		responsablesBean = r;
	}
	
    public List<ResponsableFile> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	
        List<ResponsableFile> data = new ArrayList<ResponsableFile>();
        //List<>

        IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
        if (dataService == null || responsablesBean == null || responsablesBean.getSelectedResponsable() == null)
        {
        	return data;
        }

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("responsable1", responsablesBean.getSelectedResponsable());

        //filter & sort
        try {
			data = buildResponsableFile(dataService.getAllFiles(pageSize, first, params , sortField, SortOrder.DESCENDING.equals(sortOrder)));
			
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
 	 
        //rowCount
        int dataSize = 0;
		try {
			dataSize = (int)dataService.getFilesCount(params);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
        this.setRowCount(dataSize);

        return data;
    }

	private List<ResponsableFile> buildResponsableFile(List<Expediente> allFiles) throws DataException {
		List<ResponsableFile> result = new ArrayList<ResponsableFile>();
		for (Expediente file : allFiles) {
			ResponsableFile f = buildResponsableFile(file);
			result.add(f);
		}
		return result;
	}

	private ResponsableFile buildResponsableFile(Expediente file) throws DataException {
		IDataService dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		Expediente exp = dataService.getFileById(file.getId(), "clientes", "pagos");
		
		ResponsableFile result = new ResponsableFile();
		result.setId(exp.getId());
		if (!exp.getClientes().isEmpty())
		{
			Cliente client = exp.getClientes().iterator().next();			
			result.setSurname(client.getApellidos());
			result.setName(client.getNombre());
			result.setPhone(client.getTelefono());
		}
		result.setSubject(file.getAsunto());
		
		if (!exp.getPagos().isEmpty()) {
			Iterator<Pago> p = exp.getPagos().iterator();
			double pago = 0.0;
			while(p.hasNext()) {
				pago += p.next().getCantidad().doubleValue();
			}
			result.setPayment(pago);
		}
		
		result.setClosed(file.getCerrado());
		
		return result;
	}	

}

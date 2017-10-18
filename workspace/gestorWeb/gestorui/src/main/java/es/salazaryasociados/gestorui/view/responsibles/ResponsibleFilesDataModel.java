package es.salazaryasociados.gestorui.view.responsibles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ResponsibleDTO;
import es.salazaryasociados.services.data.dto.ResponsibleFilesDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import lombok.Getter;
import lombok.Setter;

public class ResponsibleFilesDataModel extends LazyDataModel<ResponsibleFilesDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -308396929723544798L;
	private IDataService dataService;

	/**
	 * Responsable por el que filtrar
	 */
	@Getter @Setter
	private ResponsibleDTO responsible;
	@Getter @Setter
	private Boolean includeClosed = new Boolean(false);
	@Getter @Setter
	private Boolean includeResponsible2 = new Boolean(false);
	
	public ResponsibleFilesDataModel(IDataService dataSrv) {
		dataService = dataSrv;
	}

	@Override
	public ResponsibleFilesDTO getRowData(String rowKey) {
		ResponsibleFilesDTO result = null;
		return result;
	}

	@Override
	public Object getRowKey(ResponsibleFilesDTO exp) {
		return new Long(exp.getExpId());
	}

	@Override
	public List<ResponsibleFilesDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<ResponsibleFilesDTO> data = new ArrayList<ResponsibleFilesDTO>();

		if (dataService != null) {
			try {
				
				// De momento es obligatorio seleccionar responsable:
				if (responsible == null) {
					return null;
				}
				
		        // Siempre se filtra por responsable
		        Map<String,Object> filtersAux = new HashMap<String, Object>(filters);
		        
		        if (responsible != null)
		        	filtersAux.put("responsable1", responsible);
		        
		        if (includeResponsible2) {
		        	filtersAux.remove("responsable1");
		        	filtersAux.put("responsable2", responsible);
		        }
		        							        
				
		        if (!includeClosed) {
					filtersAux.put("cerrado", new Boolean(false));
				}
		        
				data = dataService.getAllResponsibleFiles(pageSize, first, filtersAux, sortField,
						SortOrder.DESCENDING.equals(sortOrder));
				// rowCount
				int dataSize = (int) dataService.getResponsibeFilesCount(filtersAux);
				this.setRowCount(dataSize);
			} catch (DataServiceException e) {
				this.setRowCount(0);
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			}
		}
		return data;

	}
}

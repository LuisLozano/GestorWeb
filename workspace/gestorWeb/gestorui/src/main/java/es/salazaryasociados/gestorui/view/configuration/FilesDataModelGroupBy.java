package es.salazaryasociados.gestorui.view.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.SortOrder;

import es.salazaryasociados.gestorui.view.files.FilesDataModel;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

public class FilesDataModelGroupBy extends FilesDataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148162891895509256L;
	private IDataService dataService;
	private String groupBy;
	private String suggestion;
	private Map<Integer, FileSummaryDTO> resultData = new HashMap<Integer, FileSummaryDTO>();

	public FilesDataModelGroupBy(IDataService dataSrv) {
		super(dataSrv);
		dataService = dataSrv;
	}
	
	@Override
	public FileSummaryDTO getRowData(String rowKey) {
		FileSummaryDTO result = resultData.get(Integer.parseInt(rowKey));
		return result;
	}

	@Override
	public Object getRowKey(FileSummaryDTO exp) {
		return new Integer(exp.getId());
	}
	
	@Override
	public List<FileSummaryDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<FileSummaryDTO> data = new ArrayList<FileSummaryDTO>();

		if (dataService != null) {
			try {
				
				if (suggestion != null) {
					filters.put("asunto", suggestion);
				}
				
				data = dataService.getFilesGroupBy(pageSize, first, filters, sortField,
						SortOrder.DESCENDING.equals(sortOrder), groupBy);
				// rowCount
				int dataSize = (int) dataService.getlFilesCount(filters, groupBy);								
				this.setRowCount(dataSize);
				
				resultData.clear();
				for (FileSummaryDTO file : data) {
					resultData.put(file.getId(), file);
				}
				
			} catch (DataServiceException e) {
				this.setRowCount(0);
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			}
		}
		return data;

	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}

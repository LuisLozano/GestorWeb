package es.salazaryasociados.gestorui.view.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

public class FilesDataModel extends LazyDataModel<FileSummaryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148162891895509256L;
	private IDataService dataService;
	private Map<Integer, FileSummaryDTO> resultData = new HashMap<Integer, FileSummaryDTO>();

	public FilesDataModel(IDataService dataSrv) {
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
				data = dataService.getAllFiles(pageSize, first, filters, sortField,
						SortOrder.DESCENDING.equals(sortOrder));
				// rowCount
				int dataSize = (int) dataService.getlFilesCount(filters);
				resultData.clear();
				for (FileSummaryDTO file : data) {
					resultData.put(file.getId(), file);
				}
				
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

package es.salazaryasociados.gestorui.view.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

public class SubjectsDataModel extends LazyDataModel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148162891895509256L;
	private IDataService dataService;

	public SubjectsDataModel(IDataService dataSrv) {
		dataService = dataSrv;
	}

	@Override
	public String getRowData(String rowKey) {
		return rowKey;
	}

	@Override
	public Object getRowKey(String subject) {
		return subject;
	}

	@Override
	public List<String> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<String> data = new ArrayList<String>();

		if (dataService != null) {
			try {
				data = dataService.getSubjects(pageSize, first, filters, sortField,
						SortOrder.DESCENDING.equals(sortOrder));
				// rowCount
				int dataSize = (int) dataService.getSubjectsCount(filters);
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

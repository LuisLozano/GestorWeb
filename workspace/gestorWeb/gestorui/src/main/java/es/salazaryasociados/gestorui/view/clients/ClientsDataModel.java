package es.salazaryasociados.gestorui.view.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

public class ClientsDataModel extends LazyDataModel<ClientSummaryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148162891895509256L;
	private IDataService dataService;

	public ClientsDataModel(IDataService dataSrv) {
		dataService = dataSrv;
	}

	@Override
	public ClientSummaryDTO getRowData(String rowKey) {
		ClientSummaryDTO result = null;
		return result;
	}

	@Override
	public Object getRowKey(ClientSummaryDTO exp) {
		return new Long(exp.getId());
	}

	@Override
	public List<ClientSummaryDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<ClientSummaryDTO> data = new ArrayList<ClientSummaryDTO>();

		if (dataService != null) {
			try {
				data = dataService.getAllClients(pageSize, first, filters, sortField,
						SortOrder.DESCENDING.equals(sortOrder));
				// rowCount
				int dataSize = (int) dataService.getlClientsCount(filters);
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

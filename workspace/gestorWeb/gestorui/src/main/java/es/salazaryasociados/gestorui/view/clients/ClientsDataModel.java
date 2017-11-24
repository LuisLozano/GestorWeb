package es.salazaryasociados.gestorui.view.clients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

public class ClientsDataModel extends LazyDataModel<ClientSummaryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148162891895509256L;
	private IDataService dataService;
	private Integer suggestClient;

	public ClientsDataModel(IDataService dataSrv) {
		dataService = dataSrv;
	}

	@Override
	public ClientSummaryDTO getRowData(String rowKey) {
		ClientSummaryDTO result = null;
		try {
			Integer id = Integer.parseInt(rowKey);
			ClientDTO client = dataService.getClientById(id);
			if (client != null) {
				result = new ClientSummaryDTO();
				result.setId(client.getId());
			}
		} catch (Exception e) {
			// TODO: show log
		}
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

		try {
			if (dataService != null) {

				if (suggestClient != null) {
					Map<String, Object> filter = new HashMap<String, Object>();
					filter.put("id", suggestClient);
					data = dataService.getAllClients(pageSize, first, filter, sortField,
							SortOrder.DESCENDING.equals(sortOrder));
				} else {
					data = dataService.getAllClients(pageSize, first, filters, sortField,
							SortOrder.DESCENDING.equals(sortOrder));
					// rowCount
					int dataSize = (int) dataService.getlClientsCount(filters);
					this.setRowCount(dataSize);
				}
			}
		} catch (DataServiceException e) {
			this.setRowCount(0);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
		}
		return data;
	}

	public void setId(Integer suggestClient) {
		this.suggestClient = suggestClient;
	}
}

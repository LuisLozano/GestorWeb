package es.salazaryasociados.services.data.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.dto.DocumentDTO;
import es.salazaryasociados.services.data.dto.EventDTO;
import es.salazaryasociados.services.data.dto.FileDTO;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.dto.PaymentDTO;
import es.salazaryasociados.services.data.dto.ResponsibleDTO;
import es.salazaryasociados.services.data.dto.ResponsibleFilesDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

public interface IDataService {
	
	//Expedientes
	public List<FileSummaryDTO> getAllFiles(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException;
	public List<ResponsibleFilesDTO> getAllResponsibleFiles(int pageSize, int first, Map<String, Object> filters,
			String sortField, boolean equals) throws DataServiceException;	
	public long	getlFilesCount(final Map<String, Object> params) throws DataServiceException;
	public long getResponsibeFilesCount(Map<String, Object> filtersAux) throws DataServiceException;
	public FileDTO getFileById(int id, String ... fetch) throws DataServiceException;
	public Integer saveFile(FileDTO file) throws DataServiceException;
	public void deleteFile(int fileID) throws DataServiceException;
	
	public List<FileSummaryDTO> getFilesGroupBy(int pageSize, int first, final Map<String, Object> params, String order, boolean desc, String groupBy) throws DataServiceException;
	public long getlFilesCount(Map<String, Object> filters, String groupBy);
	public List<String> getSubjects(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException;	
	public void unifySubjects(List<String> selectedSubjects, String newSubject) throws DataServiceException;
	
	// Eventos
	public List<EventDTO> getAllEvents(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException;
	public Integer saveEvent(EventDTO event) throws DataServiceException;
	public void deleteEvent(int eventID) throws DataServiceException;
	public List<EventDTO> getAllEventsFromResponsible(Integer responsibleID) throws DataServiceException;
	public List<EventDTO> getAllEvents(Date startDate, Date endDate);
	
	//Responsables
	public List<ResponsibleDTO> getAllResponsibles(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException;
	public Integer saveResponsible(ResponsibleDTO responsible) throws DataServiceException;
	public void deleteResponsible(ResponsibleDTO selectedResponsible) throws DataServiceException;
	
	//Clientes
	public List<ClientSummaryDTO> getAllClients(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException;
	public long	getlClientsCount(final Map<String, Object> params) throws DataServiceException;
	public ClientDTO getClientById(int id, String ... fetch) throws DataServiceException;
	public Integer saveClient(ClientDTO file) throws DataServiceException;
	public void deleteClient(int clientID) throws DataServiceException;
	public void unifyClients(int clientID1, int clientID2) throws DataServiceException;
	public List<Integer[]> getDuplicateClients() throws DataServiceException;
	
	//Pagos
	public List<PaymentDTO> getAllPayments(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException;
	public Integer savePayment(PaymentDTO payment) throws DataServiceException;
	public void deletePayment(int paymentID) throws DataServiceException;	
	
	//Documentos
	public Integer saveDocument(DocumentDTO file) throws DataServiceException;
	public void deleteDocument(int documentID) throws DataServiceException;
		
}

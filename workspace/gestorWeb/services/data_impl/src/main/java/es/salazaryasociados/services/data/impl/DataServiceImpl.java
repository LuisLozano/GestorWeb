package es.salazaryasociados.services.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.text.similarity.LevenshteinDistance;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.salazaryasociados.control.ClientDao;
import es.salazaryasociados.control.DocumentoDao;
import es.salazaryasociados.control.EventoDao;
import es.salazaryasociados.control.ExpedienteDao;
import es.salazaryasociados.control.ListadoExpRespDao;
import es.salazaryasociados.control.PagoDao;
import es.salazaryasociados.control.ResponsableDao;
import es.salazaryasociados.control.exceptions.DataException;
import es.salazaryasociados.model.Cliente;
import es.salazaryasociados.model.Documento;
import es.salazaryasociados.model.Evento;
import es.salazaryasociados.model.Expediente;
import es.salazaryasociados.model.ListadoExpResp;
import es.salazaryasociados.model.Pago;
import es.salazaryasociados.model.Responsable;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.dto.DocumentDTO;
import es.salazaryasociados.services.data.dto.DocumentSummaryDTO;
import es.salazaryasociados.services.data.dto.EventDTO;
import es.salazaryasociados.services.data.dto.FileDTO;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.dto.PaymentDTO;
import es.salazaryasociados.services.data.dto.PaymentSummaryDTO;
import es.salazaryasociados.services.data.dto.ResponsibleDTO;
import es.salazaryasociados.services.data.dto.ResponsibleFilesDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import es.salazaryasociados.services.data.exceptions.GestorErrors;
import es.salazaryasociados.services.data.impl.dto.ClientDTOConverter;
import es.salazaryasociados.services.data.impl.dto.ClientSummaryDTOConverter;
import es.salazaryasociados.services.data.impl.dto.DocumentDTOConverter;
import es.salazaryasociados.services.data.impl.dto.DocumentSummaryDTOConverter;
import es.salazaryasociados.services.data.impl.dto.EventDTOConverter;
import es.salazaryasociados.services.data.impl.dto.FileDTOConverter;
import es.salazaryasociados.services.data.impl.dto.FileSummaryDTOConverter;
import es.salazaryasociados.services.data.impl.dto.PaymentDTOConverter;
import es.salazaryasociados.services.data.impl.dto.PaymentSummaryDTOConverter;
import es.salazaryasociados.services.data.impl.dto.RespFileDTOConverter;
import es.salazaryasociados.services.data.impl.dto.ResponsibleDTOConverter;
import lombok.Getter;
import lombok.Setter;

public class DataServiceImpl implements IDataService {

	@Getter @Setter
	private ExpedienteDao fileDao;
	@Getter @Setter
	private EventoDao eventDao;
	@Getter @Setter
	private ResponsableDao responsibleDao;
	@Getter @Setter
	private ClientDao clientDao;
	@Getter @Setter
	private PagoDao paymentDao;
	@Getter @Setter
	private ListadoExpRespDao respFileDao;
	@Getter @Setter
	private DocumentoDao documentDao;
	
	private FileSummaryDTOConverter fileSummaryConverter = new FileSummaryDTOConverter(Expediente.class, FileSummaryDTO.class);
	private ResponsibleDTOConverter responsibleConverter = new ResponsibleDTOConverter(Responsable.class, ResponsibleDTO.class);
	private ClientSummaryDTOConverter clientSummaryConverter = new ClientSummaryDTOConverter(Cliente.class, ClientSummaryDTO.class);
	private ClientDTOConverter clientConverter = new ClientDTOConverter(Cliente.class, ClientDTO.class);
	private FileDTOConverter fileConverter = new FileDTOConverter(Expediente.class, FileDTO.class);	
	private PaymentSummaryDTOConverter paymentSummaryConverter = new PaymentSummaryDTOConverter(Pago.class, PaymentSummaryDTO.class);
	private PaymentDTOConverter paymentConverter = new PaymentDTOConverter(Pago.class, PaymentDTO.class);
	private RespFileDTOConverter respFileConverter = new RespFileDTOConverter(ListadoExpResp.class, ResponsibleFilesDTO.class);
	private DocumentDTOConverter documentConverter = new DocumentDTOConverter(Documento.class, DocumentDTO.class);
	private DocumentSummaryDTOConverter documentSummaryConverter = new DocumentSummaryDTOConverter(Documento.class, DocumentSummaryDTO.class);
	private EventDTOConverter eventConverter = new EventDTOConverter(Evento.class, EventDTO.class);
	
	public DataServiceImpl() {
		fileConverter.setClientSummaryConverter(clientSummaryConverter);
		fileConverter.setPaymentSummaryConverter(paymentSummaryConverter);
		fileConverter.setResponsibleConverter(responsibleConverter);
		fileConverter.setDocumentSummaryConverter(documentSummaryConverter);
		fileConverter.setEventDTOConverter(eventConverter);
		paymentConverter.setClientSummaryConverter(clientSummaryConverter);
		paymentConverter.setFileConverter(fileSummaryConverter);
		respFileConverter.setResponsibleConverter(responsibleConverter);
		
		clientConverter.setFileConverter(fileSummaryConverter);
		clientConverter.setDocumentSummaryConverter(documentSummaryConverter);
		eventConverter.setFileConverter(fileSummaryConverter);
	}
	
	@Override
	public List<FileSummaryDTO> getAllFiles(int pageSize, int first, Map<String, Object> params, String order,
			boolean desc) throws DataServiceException {

		List<FileSummaryDTO> result = null;

		try {
			if (params != null && params.containsKey("id")) {
				result = new ArrayList<FileSummaryDTO>();

				Expediente exp = fileDao.getById(Integer.parseInt((String) params.get("id")));
				if (exp != null) {
					result.add(fileSummaryConverter.getDto(exp));
				}
			} else {
				
				Map<String, Object> filterAux = new HashMap<String, Object>(params);
				if (params != null)
				{
					if (params.containsKey("responsable")) {						
						
						filterAux.remove("responsable");
						
						Map<String, Object> responsibleMap = new HashMap<String, Object>();
						responsibleMap.put("nombre", params.get("responsable"));
						List <Responsable> respList = responsibleDao.getAll(1, 0, responsibleMap, null, false);
						if (respList != null && respList.size() > 0)
							filterAux.put("responsable1", respList.get(0));
						else
							filterAux.put("responsable1", null);
					}				
				}
				result = fileSummaryConverter.getDtoList(fileDao.getAll(pageSize, first, filterAux, order, desc));
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}
		return result;
	}

	@Override
	public List<ResponsibleFilesDTO> getAllResponsibleFiles(int pageSize, int first, Map<String, Object> params,
			String order, boolean desc) throws DataServiceException {

		Map<String, Object> filterAux = new HashMap<String, Object>();
		if (params != null)
			filterAux.putAll(params);
		List<ResponsibleFilesDTO> result = null;
		
		try {
			if (params != null && params.containsKey("expId")) {
	
				result = new ArrayList<ResponsibleFilesDTO>();
	
				ListadoExpResp exp = respFileDao.getById(Integer.parseInt((String) params.get("expId")));
				if (exp != null) {
					result.add(respFileConverter.getDto(exp));
				}
			} else if (params != null) {
				if (params.containsKey("responsable1")) {
					ResponsibleDTO respDTO = (ResponsibleDTO) params.get("responsable1");
					Responsable resp = responsibleDao.getById(respDTO.getId());
					filterAux.put("responsable1", resp);
				}
				if (params.containsKey("responsable2")) {
					ResponsibleDTO respDTO = (ResponsibleDTO) params.get("responsable2");
					Responsable resp = responsibleDao.getById(respDTO.getId());
					filterAux.put("responsable2", resp);
				}
				
				result = respFileConverter
						.getDtoList(respFileDao.getAll(pageSize, first, filterAux, order, desc));
			} else {
				result = respFileConverter
						.getDtoList(respFileDao.getAll(pageSize, first, filterAux, order, desc));
			}
	
			// Para cada elemento se calcula lo pagado:
			for (ResponsibleFilesDTO file : result) {
				calculePayment(file);
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}
		return result;
	}
	
	private void calculePayment(ResponsibleFilesDTO file) throws DataException {
		
		Expediente realFile = fileDao.getById(file.getExpId());
		for (Pago payment : realFile.getPagos()) {
			file.setPagado(file.getPagado().add(payment.getCantidad()));
		}
	}

	public long getResponsibeFilesCount(Map<String, Object> params) throws DataServiceException {
		
		try {
			Map<String, Object> filterAux = new HashMap<String, Object>();
			if (params != null)
			{
				filterAux.putAll(params);
				if (params.containsKey("expId")) {
					ListadoExpResp exp = respFileDao.getById(Integer.parseInt((String) params.get("expId")));
					if (exp != null) {
						return 1;
					}
					else {
						return 0;
					}
				}
				if (params.containsKey("responsable1")) {
					ResponsibleDTO respDTO = (ResponsibleDTO)params.get("responsable1");
					Responsable resp = responsibleDao.getById(respDTO.getId());
					filterAux.put("responsable1", resp);
				}
				if (params.containsKey("responsable2")) {
					ResponsibleDTO respDTO = (ResponsibleDTO)params.get("responsable2");
					Responsable resp = responsibleDao.getById(respDTO.getId());
					filterAux.put("responsable2", resp);
				}
				
			}
			return respFileDao.getCount(filterAux);
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 
	}
	
	@Override
	public long getlFilesCount(Map<String, Object> params) throws DataServiceException {
		
		long result = 0;
		
		try {
			if (params != null && params.containsKey("id")) {
	
				Expediente exp = fileDao.getById(Integer.parseInt((String) params.get("id")));
				if (exp != null) {
					result = 1;
				}
			} else {
				
				Map<String, Object> filterAux = new HashMap<String, Object>(params);
				if (params != null)
				{
					if (params.containsKey("responsable")) {						
						
						filterAux.remove("responsable");
						
						Map<String, Object> responsibleMap = new HashMap<String, Object>();
						responsibleMap.put("nombre", params.get("responsable"));
						List <Responsable> respList = responsibleDao.getAll(1, 0, responsibleMap, null, false);
						if (respList != null && respList.size() > 0)
							filterAux.put("responsable1", respList.get(0));
						else
							filterAux.put("responsable1", null);
					}				
				}
				
				result = fileDao.getCount(filterAux);
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 
		return result;
	}

	@Override
	public FileDTO getFileById(int id, String... fetch) throws DataServiceException {
		try {
			return fileConverter.getDto(fileDao.getById(id, fetch));
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 		
	}

	@Override
	public Integer saveFile(FileDTO file) throws DataServiceException {
		
		Integer result;
		try {
			if (file.getId() != null && file.getId() > 0) {
				result = fileDao.update(fileConverter.getEntity(file)).getId();
			}
			else {
				int i = (int)fileDao.getLastFileNumber();
				file.setId(i+1);
				file.setFechaApertura(new Date());
				result = fileDao.persist(fileConverter.getEntity(file)).getId();
			}
		}catch(ValidationException e) {
			throw new DataServiceException(GestorErrors.VALIDATE_EXCEPTION, e.getMessage());			
		}catch(Throwable e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
		
		return result;
	}
	
	@Override
	public void deleteFile(int fileID) throws DataServiceException {
		
		try {
			Expediente file = fileDao.getById(fileID);
			if (file != null) {
				fileDao.delete(file);
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 			
	}
	
	@Override
	public List<FileSummaryDTO> getFilesGroupBy(int pageSize, int first, final Map<String, Object> params, String order, boolean desc, String groupBy) throws DataServiceException {
		try {
			return fileSummaryConverter.getDtoList(fileDao.getFilesGroupBy(pageSize, first, params, order, desc, groupBy));
		}catch(Exception e) {
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
	}

	@Override
	public List<String> getSubjects(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException {
		try {
			return fileDao.getSubjects(pageSize, first, params, order, desc);
		}catch(Exception e) {
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
	}
	
	@Override
	public long getlFilesCount(Map<String, Object> filters, String groupBy) {
		return fileDao.getCount(filters, groupBy);
	}
		
	@Override
	public void unifySubjects(List<String> selectedSubjects, String newSubject) throws DataServiceException {
		try {
		fileDao.unify(selectedSubjects, newSubject);
		}catch(Exception e) {
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}				
	}

	@Override
	public Integer saveEvent(EventDTO event) throws DataServiceException {
		
		Integer result;
		try {
			if (event.getId() != null && event.getId() > 0) {
				result = eventDao.update(eventConverter.getEntity(event)).getId();
			}
			else {
				result = eventDao.persist(eventConverter.getEntity(event)).getId();
			}
		}catch(ValidationException e) {
			throw new DataServiceException(GestorErrors.VALIDATE_EXCEPTION, e.getMessage());			
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
		
		return result;
	}

	@Override
	public void deleteEvent(int eventID) throws DataServiceException {
		try {
			Evento event = eventDao.getById(eventID);
			eventDao.delete(event);
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}			
	}
	
	@Override
	public List<EventDTO> getAllEvents(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException {
		try {
			return eventConverter.getDtoList(eventDao.getAll(pageSize, first, params, order, desc));
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 			
	}	
	
	@Override
	public List<EventDTO> getAllEvents(Date startDate, Date endDate) {
		return eventConverter.getDtoList(eventDao.getAllEvents(startDate, endDate));
	}
	
	@Override
	public List<ResponsibleDTO> getAllResponsibles(int pageSize, int first, Map<String, Object> params, String order,
			boolean desc) throws DataServiceException {
		try {
			return responsibleConverter.getDtoList(responsibleDao.getAll(pageSize, first, params, order, desc));
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 			
	}

	@Override
	public Integer saveResponsible(ResponsibleDTO responsible) throws DataServiceException {
		
		Integer result;
		try {
			if (responsible.getId() != null && responsible.getId() > 0) {
				result = responsibleDao.update(responsibleConverter.getEntity(responsible)).getId();
			}
			else {
				result = responsibleDao.persist(responsibleConverter.getEntity(responsible)).getId();
			}
		}catch(ValidationException e) {
			throw new DataServiceException(GestorErrors.VALIDATE_EXCEPTION, e.getMessage());			
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}		
		
		return result;
	}
	
	@Override
	public void deleteResponsible(ResponsibleDTO resp) throws DataServiceException {
		
		try {
			Responsable respItem = responsibleDao.getById(resp.getId());
			if (respItem != null) {
				responsibleDao.delete(respItem);
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 		
	}	
	
	@Override
	public List<EventDTO> getAllEventsFromResponsible(Integer responsibleID) throws DataServiceException {
		List<EventDTO> result = new ArrayList<EventDTO>();
		try {
			Responsable resp = responsibleDao.getById(responsibleID);
			result = eventConverter.getDtoList(eventDao.getAllEventsFromResponsible(resp));
		} catch(Exception e) {
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
		
		return result;
	}
	
	@Override
	public List<ClientSummaryDTO> getAllClients(int pageSize, int first, Map<String, Object> params, String order,
			boolean desc) throws DataServiceException {
		try {
			List<ClientSummaryDTO> result = clientSummaryConverter.getDtoList(clientDao.getAll(pageSize, first, params, order, desc));
			return result;
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 		
	}

	@Override
	public long getlClientsCount(Map<String, Object> params) throws DataServiceException {
		try {
			return clientDao.getCount(params);
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 					
	}
	
	@Override
	public ClientDTO getClientById(int id, String... fetch) throws DataServiceException {
		try {
			return clientConverter.getDto(clientDao.getById(id, fetch));
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 					
	}	

	@Override
	public Integer saveClient(ClientDTO client) throws DataServiceException {
		
		Integer result;
		try {
			if (client.getId() != null && client.getId() > 0) {
				result = clientDao.update(clientConverter.getEntity(client)).getId();
			}
			else {
				result = clientDao.persist(clientConverter.getEntity(client)).getId();
			}
		}catch(ValidationException e) {
			throw new DataServiceException(GestorErrors.VALIDATE_EXCEPTION, e.getMessage());
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
		
		return result;
	}	
	
	@Override
	public void deleteClient(int clientID) throws DataServiceException {
		try {
			Cliente client = clientDao.getById(clientID);
			if (client != null) {
				clientDao.delete(client);
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 					
	}	
	
	@Override
	public void unifyClients(int clientID1, int clientID2) throws DataServiceException {
		if (clientID1 == clientID2) {
			throw new DataServiceException(GestorErrors.UNIFY_SAME_CLIENT, "Deben seleccionarse dos clientes distintos");
		}
		
		try {
			Cliente client1 = clientDao.getById(clientID1);
			Cliente client2 = clientDao.getById(clientID2);
			
			if (client1 == null)
			{
				throw new DataServiceException(GestorErrors.UNIFY_CLIENT_NOT_FOUND, "Identificador " + clientID1);
			}
			
			if (client2 == null)
			{
				throw new DataServiceException(GestorErrors.UNIFY_CLIENT_NOT_FOUND, "Identificador " + clientID2);
			}

			for(Documento doc : client2.getDocumentos()) {
				doc.setCliente(client1);
				documentDao.update(doc);
			}

			for(Pago p : client2.getPagos()) {
				p.setPagador(client1);
				paymentDao.update(p);
			}			
			
			for(Expediente exp : client2.getExpedientes()) {
				exp.getClientes().add(client1);
				fileDao.update(exp);
			}			
			
			client2.setExpedientes(null);
			client2.setPagos(null);
			client2.setDocumentos(null);
			
			client1.setObservaciones(addObs(client1, client2));
			
			deleteClient(clientID2);
			
			clientDao.update(client1);
			
		} catch (DataException e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}		
	}
	
	private String addObs(Cliente client1, Cliente client2) {
		
		String obs1 = "";
		if (client1 != null && client1.getObservaciones() != null) {
			obs1 = client1.getObservaciones();
		}
		
		String obs2 = "";
		if (client2 != null && client2.getObservaciones() != null) {
			obs2 = client2.getObservaciones();
		}
		
		String address2 = "";
		if (client2 != null && client2.getDireccion() != null) {
			address2 = client2.getDireccion();
		}
		
		String poblacion2 = "";
		if (client2 != null && client2.getPoblacion() != null) {
			address2 = client2.getPoblacion();
		}				
		
		
		//return obs1 + "\n__ UNIFICACION __\n" + obs2 + "\n" + address2 + "\n" + poblacion2;
		return obs1 + "\n__ UNIFICACION __\n" + serializedClient(client2);
	}

	private String serializedClient(Cliente client2) {
		
		String result = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			result = objectMapper.writeValueAsString(client2);
		}catch(Exception e) {
			return "ERROR AL MAPEAR";
		}
		return result;
	}

	public List<Integer[]> getDuplicateClients() throws DataServiceException {
		List<Integer[]> result = new ArrayList<Integer[]>();
		
		int first1 = 0;
		int c1Ind = 0;
		List<ClientSummaryDTO> c1List = getAllClients(-1, first1, null, "id", false);						
		while (c1Ind < c1List.size()) {			
			ClientSummaryDTO client1 = c1List.get(c1Ind);
			int c2Ind = c1Ind + 1;
			while (c2Ind < c1List.size()) {					
				ClientSummaryDTO client2 = c1List.get(c2Ind);
				if (client2.getDni().equals(client2.getId().toString()) && distance(client1, client2) < 3) {
					Integer[] pair = {client1.getId(), client2.getId()};
					result.add(pair);
				}
				c2Ind++;
			}				
			c1Ind++;
		}
		
		return result;
	}
	
	private int distance(ClientSummaryDTO client1, ClientSummaryDTO client2) {
		
		String name1 = client1.getNombre().toLowerCase().replaceAll("\\s+","");
		String name2 = client2.getNombre().toLowerCase().replaceAll("\\s+","");		
		String surname1 = client1.getApellidos().toLowerCase().replaceAll("\\s+","");
		String surname2 = client2.getApellidos().toLowerCase().replaceAll("\\s+","");
		
		LevenshteinDistance l = LevenshteinDistance.getDefaultInstance();
		int i = l.apply(name1 + surname1, name2 + surname2);

		return i;
	}

	@Override
	public Integer savePayment(PaymentDTO payment) throws DataServiceException {
		
		Integer result;
		try {
			if (payment.getId() != null && payment.getId() > 0) {
				result = paymentDao.update(paymentConverter.getEntity(payment)).getId();
			}
			else {
				result = paymentDao.persist(paymentConverter.getEntity(payment)).getId();
			}
		}catch(ValidationException e) {
			throw new DataServiceException(GestorErrors.VALIDATE_EXCEPTION, e.getMessage());			
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
		
		return result;
	}

	@Override
	public void deletePayment(int paymentID) throws DataServiceException {
		try {
			Pago payment = paymentDao.getById(paymentID);
			paymentDao.delete(payment);
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}			
	}
	
	@Override
	public List<PaymentDTO> getAllPayments(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataServiceException {
		try {
			return paymentConverter.getDtoList(paymentDao.getAll(pageSize, first, params, order, desc));
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 					
	}

	@Override
	public Integer saveDocument(DocumentDTO document) throws DataServiceException {
		
		Integer result;
		try {
			if (document.getId() != null && document.getId() > 0) {
				result = documentDao.update(documentConverter.getEntity(document)).getId();
			}
			else {
				result = documentDao.persist(documentConverter.getEntity(document)).getId();
			}
		}catch(ValidationException e) {
			throw new DataServiceException(GestorErrors.VALIDATE_EXCEPTION, e.getMessage());			
		}catch(Exception e){
			throw new DataServiceException(GestorErrors.PERSISTENCE_EXCEPTION, e);
		}
		
		return result;
	}

	@Override
	public void deleteDocument(int documentID) throws DataServiceException {
		try {
			Documento document = documentDao.getById(documentID);
			if (document != null) {
				documentDao.delete(document);
			}
		} catch (Exception e) {
			throw new DataServiceException(GestorErrors.UNKNOWN_EXCEPTION, e);
		}				 					
	}

}

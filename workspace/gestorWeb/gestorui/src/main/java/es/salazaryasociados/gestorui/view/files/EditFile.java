package es.salazaryasociados.gestorui.view.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.omg.CORBA.UserException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.gestorui.view.clients.ClientsDataModel;
import es.salazaryasociados.services.data.api.IDataService;
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
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class EditFile extends SalazarBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8570859863451916523L;
	
	private static final String ACTION_BUTTON = "actionButton";
	private static final String DELETE = "delete";
	private static final String UPDATE = "update";
	private static final String SAVE = "save";
	private static final String INIT_CREATE = "initCreate";

	@Getter @Setter
	private FileDTO fileDetail;	
	@Getter @Setter
	private FileSummaryDTO fileSummary;	
	@Getter @Setter
	private ClientsDataModel allClients;
	@Getter @Setter
	private PaymentDTO newPayment = new PaymentDTO(); 
		
	private String actionButton = null;
	
	@Getter
	private List<ResponsibleDTO> responsibles = new ArrayList<ResponsibleDTO>();
	@Inject
	protected IDataService dataService;

	@Getter
	private UploadedFile file;
	
	@Getter @Setter
	private DocumentSummaryDTO downloadDocument;
	
	@Getter @Setter
	private ScheduleModel lazyEventModel;
	
	@Getter @Setter
	private TimelineModel timeLineModel;
	
	@Getter @Setter
	private FileEvent event;
	
	@PostConstruct
	public void init() {
		super.init();
		
		initData();
	}
	
	private void initData() {
		try {
			responsibles = dataService.getAllResponsibles(-1, 0, null, null, false);
			allClients = new ClientsDataModel(dataService);
			
			loadEvents();
			
		} catch (DataServiceException e) {
			responsibles = new ArrayList<ResponsibleDTO>();
		}
	}

	private void loadEvents() {
		List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
		List<TimelineEvent> tevents = new ArrayList<TimelineEvent>();
		
		if (fileDetail != null && fileDetail.getEvents() != null && fileDetail.getEvents().size() > 0) {
			
			for (EventDTO eventDTO : fileDetail.getEvents()) {
				
				FileEvent e = new FileEvent(eventDTO);
				events.add(e);
				
				TimelineEvent tevent = new TimelineEvent(eventDTO.getTitulo(), eventDTO.getFechaInicio());
				tevents.add(tevent);
			}
		}		
		lazyEventModel = new DefaultScheduleModel(events);
		timeLineModel = new TimelineModel(tevents);
		
	}

	public void setRespFile(ResponsibleFilesDTO file) {
		fileSummary = new FileSummaryDTO();
		fileSummary.setAsunto(file.getAsunto());
		fileSummary.setCerrado(file.getCerrado());
		fileSummary.setId(file.getExpId());
		fileSummary.setPresupuesto(file.getPresupuesto());
		fileSummary.setPagado(file.getPagado());
		fileSummary.setObservaciones(file.getObservaciones());
		fileSummary.setMoroso(file.getPresupuesto().subtract(file.getPagado()));
		fileSummary.setResponsable(file.getResponsible1().getNombre());
	}
	
	public void setFile(UploadedFile file) {
		this.file = file;
	}	
	
	
	public void handleFileUpload(FileUploadEvent event) {
		this.file = event.getFile();
		upload();
    }
	
	public void upload() {
        		
		if(file != null) {
        
			if (fileDetail.getId() == null) {
				save();
			}
			
        	String path = "../../Exp_" + fileSummary.getId();
            Path copyFile = Paths.get(path + "/" + file.getFileName());
            try {
            	File newFile = new File(path);
            	newFile.mkdirs();
            	
				Files.copy(file.getInputstream(), copyFile);
				
				DocumentSummaryDTO document = new DocumentSummaryDTO();
				
				//TODO: introducir una descripción
				document.setDescripcion(file.getFileName());
				document.setRuta(copyFile.toString());
				fileDetail.getDocuments().add(document);
				
				DocumentDTO documentDTO = new DocumentDTO();
				//TODO: introducir una descripción
				documentDTO.setDescripcion(file.getFileName());
				documentDTO.setRuta(copyFile.toString());
				documentDTO.setFile(fileSummary);
				
				Integer id = dataService.saveDocument(documentDTO);
				document.setId(id);
			} catch (IOException | DataServiceException e) {
	            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage());
	            FacesContext.getCurrentInstance().addMessage(null, message);
			}
        }
    }	
	
	public void deleteDocument(DocumentSummaryDTO document) {		
		try {
			dataService.deleteDocument(document.getId());
			
			Path path = Paths.get(document.getRuta());
			Files.delete(path);
			
			//TODO: no sé porqué no funciona 
			//fileDetail.getDocuments().remove(document);			
			Set<DocumentSummaryDTO> documents = new HashSet<DocumentSummaryDTO>();
			for (DocumentSummaryDTO summ : fileDetail.getDocuments()) {
				if (!summ.getId().equals(document.getId())) {
					documents.add(summ);
				}
			}
			fileDetail.setDocuments(documents);
			
		} catch (DataServiceException | IOException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}	
	
	/**
	 * Recoge el evento de la vista
	 * 
	 * @param event
	 * @throws UserException 
	 */
	public void attrListener(ActionEvent event) {
		actionButton = (String) event.getComponent().getAttributes().get(ACTION_BUTTON);
	}	
	
	public void addClient(ClientSummaryDTO client) {
		fileDetail.getClients().add(client);
	}
	
	public void deleteClient(ClientSummaryDTO client) {
		fileDetail.getClients().remove(client);
	}
	
	public void addPayment() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		newPayment.setFecha(timestamp);
		newPayment.setFile(fileSummary);
		newPayment.setPayer(fileDetail.getClients().iterator().next());
		try {
			Integer id = dataService.savePayment(newPayment);
			
			PaymentSummaryDTO summary = new PaymentSummaryDTO();
			summary.setCantidad(newPayment.getCantidad());
			summary.setFecha(newPayment.getFecha());
			summary.setFile(fileDetail.getId());
			summary.setObservaciones(newPayment.getObservaciones());
			summary.setPayer(newPayment.getPayer().getApellidos() + ", " + newPayment.getPayer().getNombre());
			summary.setId(id);
			
			fileDetail.getPayments().add(summary);
			
			fileSummary.setPagado(fileSummary.getPagado().add(summary.getCantidad()));
			fileSummary.setMoroso(fileSummary.getMoroso().subtract(summary.getCantidad()));
		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
		newPayment = new PaymentDTO();
	}

	public void deletePayment(PaymentSummaryDTO payment) {
		try {
			dataService.deletePayment(payment.getId());
			
			fileDetail.getPayments().remove(payment);
			fileSummary.setPagado(fileSummary.getPagado().subtract(payment.getCantidad()));
			fileSummary.setMoroso(fileSummary.getMoroso().add(payment.getCantidad()));
		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
	}
	
	public void addEvent() {
		try {
			if (event.getId() == null) {
				Integer id = dataService.saveEvent(event.buildEventDTO());
				event.setId(id.toString());
				event.getEvent().setId(id);
				lazyEventModel.addEvent(event);
				timeLineModel.add(new TimelineEvent(event.getEvent().getTitulo(), event.getStartDate()));
			}
			else {
				event.setId(dataService.saveEvent(event.buildEventDTO()).toString());
				lazyEventModel.updateEvent(event);								
				timeLineModel.update(new TimelineEvent(event.getEvent().getTitulo(), event.getStartDate()));
			}

		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
	}
	
	public void deleteEvent () {
		if (event != null && event.getId() != null) {
			EventDTO dto = event.getEvent();
			try {
				dataService.deleteEvent(dto.getId());
				lazyEventModel.deleteEvent(event);
				timeLineModel.delete(new TimelineEvent(event.getEvent().getTitulo(), event.getStartDate()));
				event = null;
			} catch (DataServiceException e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();
			}
		}
	}
		
	public void onEventSelect(SelectEvent selectEvent) {
		event = (FileEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new FileEvent("Cambiar titulo", new Timestamp(((Date)selectEvent.getObject()).getTime()), new Timestamp(((Date)selectEvent.getObject()).getTime()));
		
		EventDTO newEvent = new EventDTO();
		newEvent.setFile(fileSummary);
		event.setEvent(newEvent);
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		logger.debug("Llamada a onEventMove");
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		logger.debug("Llamada a onEventResize");
	}	
	
	public void onTimelineEventSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, timelineEvent.getData().toString(), timelineEvent.getStartDate().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
	
	private void save() {

		try {
			if (fileDetail.getClients() != null && fileDetail.getClients().size() > 0) {
				
				if (fileDetail.getFechaCierre() == null && fileDetail.getCerrado().booleanValue())
					fileDetail.setFechaCierre(new Date());
				else if (fileDetail.getFechaCierre() != null && !fileDetail.getCerrado().booleanValue())
					fileDetail.setFechaCierre(null);
				
				dataService.saveFile(fileDetail);
			} else
				throw new DataServiceException(GestorErrors.FILE_HAS_NO_CLIENTS, "No hay clientes en el expediente");
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
	}
	
	/**
	 * Controla la acción de los botones de la vista. CREA, EDITA, BORRA
	 * 
	 * @return Pantalla a la que se dirige
	 * @throws UserException 
	 */
	public void editAction() {
		FacesContext context = FacesContext.getCurrentInstance();
				
		if (actionButton != null) {
			try {				
				switch (actionButton) {				
				case INIT_CREATE:
					initData();
					
					fileDetail = new FileDTO();
					fileDetail.setId(new Integer(-1));
					fileDetail.setResponsible1(responsibles.get(0));
					fileDetail.setResponsible2(responsibles.get(0));
					fileSummary = new FileSummaryDTO();
					
					lazyEventModel = new DefaultScheduleModel();
					break;
				case UPDATE:
					fileDetail = dataService.getFileById(fileSummary.getId());
					initData();					
					break;
				case SAVE:
					save();
					break;
				}					
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();
			}
		}
		
		//return null; // Devuelve a la misma pantalla
	}
	
	public void prepDownload(DocumentSummaryDTO dto) {
		downloadDocument = dto;
	}
	
	public StreamedContent getDownloadFile() {
		try {
			File f = new File(downloadDocument.getRuta());
			if (f.exists()) {
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				FileInputStream stream = new FileInputStream(f);
		        StreamedContent downloadFile = new DefaultStreamedContent(stream, externalContext.getMimeType(f.getName()), f.getName());
		        return downloadFile;
			}
		} catch (FileNotFoundException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
		return null;
    }
	
	public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();

        if (query != null && query.trim().length() > 0)
        {
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("asunto", query);
			try {
				results = dataService.getSubjects(10, 0, params , "asunto", false);
			} catch (DataServiceException e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();
			}
        }
        return results;
    }	
}

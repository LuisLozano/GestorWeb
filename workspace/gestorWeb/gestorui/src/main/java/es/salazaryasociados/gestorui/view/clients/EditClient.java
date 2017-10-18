package es.salazaryasociados.gestorui.view.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.dto.DocumentDTO;
import es.salazaryasociados.services.data.dto.DocumentSummaryDTO;
import es.salazaryasociados.services.data.dto.FileDTO;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class EditClient extends SalazarBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8900824822531578313L;
	private static final String ACTION_BUTTON = "actionButton";
	private static final String DELETE = "delete";
	private static final String UPDATE = "update";
	private static final String SAVE = "save";
	private static final String INIT_CREATE = "initCreate";

	@Getter @Setter
	private ClientDTO clientDetail;
	@Getter @Setter
	private ClientSummaryDTO clientSummary;	
	
	private String actionButton = null;	
	@Inject
	protected IDataService dataService;
	
	@Getter
	private UploadedFile file;
	
	@Getter @Setter
	private DocumentSummaryDTO downloadDocument;	
	
	@PostConstruct
	public void init() {
		super.init();
		
		initData();
	}
	
	private void initData() {
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
	
		
	private void save() {
		try {
			dataService.saveClient(clientDetail);
		} catch (DataServiceException e) {
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
	public String editAction() throws UserException {
		FacesContext context = FacesContext.getCurrentInstance();
				
		if (actionButton != null) {
			try {
				switch (actionButton) {				
				case INIT_CREATE:
					clientDetail = new ClientDTO();
					clientSummary = new ClientSummaryDTO();					
					break;
				case UPDATE:
					clientDetail = dataService.getClientById(clientSummary.getId());
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
		
		return null; // Devuelve a la misma pantalla
	}
	
	public void setFile(UploadedFile file) {
		this.file = file;
	}	
	
	public void handleFileUpload(FileUploadEvent event) {
		this.file = event.getFile();
		upload();
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
	
	public void upload() {
        		
		if(file != null) {
        
			if (clientDetail.getId() == null) {
				save();
			}
			
        	String path = "../../Cliente_" + clientSummary.getId();
            Path copyFile = Paths.get(path + "/" + file.getFileName());
            try {
            	File newFile = new File(path);
            	newFile.mkdirs();
            	
				Files.copy(file.getInputstream(), copyFile);
				
				DocumentSummaryDTO document = new DocumentSummaryDTO();
				
				//TODO: introducir una descripción
				document.setDescripcion(file.getFileName());
				document.setRuta(copyFile.toString());
				clientDetail.getDocuments().add(document);
				
				DocumentDTO documentDTO = new DocumentDTO();
				//TODO: introducir una descripción
				documentDTO.setDescripcion(file.getFileName());
				documentDTO.setRuta(copyFile.toString());
				documentDTO.setClient(clientSummary);
				
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
			//clientDetail.getDocuments().remove(document);			
			Set<DocumentSummaryDTO> documents = new HashSet<DocumentSummaryDTO>();
			for (DocumentSummaryDTO summ : clientDetail.getDocuments()) {
				if (!summ.getId().equals(document.getId())) {
					documents.add(summ);
				}
			}
			clientDetail.setDocuments(documents);
			
		} catch (DataServiceException | IOException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}	
}

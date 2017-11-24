package es.salazaryasociados.gestorui.view.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.text.similarity.LevenshteinDistance;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.gestorui.view.clients.ClientsDataModel;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class ConfigurationBean extends SalazarBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private FilesDataModelGroupBy subjects;
	
	@Getter @Setter
	private List<FileSummaryDTO> selectedSubjects;
	
	private List<SubjectSuggestion> subjectSuggestions;
	
	@Getter @Setter
	private String newSubject;
	
	@Inject
	private IDataService dataService;
	
	@Getter
	@Setter
	private ClientsDataModel clientsDataModel1;
	
	@Getter
	@Setter
	private ClientsDataModel clientsDataModel2;	
	
	@Getter
	@Setter
	private ClientSummaryDTO selectedClient1;
	
	@Getter
	@Setter
	private ClientSummaryDTO selectedClient2;	
	
	private Integer lastClientSuggestion = new Integer(-1);

	private List<Integer[]> clientSuggestion = null;

	@Getter @Setter
	private String subjectSuggestion = "";
	
	@PostConstruct
	public void init() {
		super.init();
		
		subjects = new FilesDataModelGroupBy(dataService);
		subjects.setGroupBy("asunto");
		clientsDataModel1 = new ClientsDataModel(dataService);
		clientsDataModel2 = new ClientsDataModel(dataService);
		initData();
	}
	
	private void initData() {
	}	
	
	public String suggestSubject() {
		
		if (subjectSuggestions == null) {
			calculateSubjectSuggestions();
		}
		
		if (subjectSuggestions != null && subjectSuggestions.size() > 0) {
			subjectSuggestion = getNextSubjectSuggestion();			
			subjects.setSuggestion(subjectSuggestion);
		}		
		return null;
	}
	
	private String getNextSubjectSuggestion() {
				
		// Se elimina la sugerencia anterior
		deleteLastSuggestion();
		
		if (subjectSuggestions.size() > 0) {
			LevenshteinDistance l = LevenshteinDistance.getDefaultInstance();
			if (subjectSuggestions != null && subjectSuggestions.size() > 0) {
				int count = -1;
				String subject = "";
				for (SubjectSuggestion s : subjectSuggestions) {				
					if (s.count > 0 && s.count > count && !s.subject.equals(subjectSuggestion) && (l.apply(s.subject, subject)) > 3) {
						count = s.count;
						subjectSuggestion  = s.subject;					
					}
				}
			}
		} else {
			subjectSuggestions = null;
		}
		
		return subjectSuggestion;
	}

	private void deleteLastSuggestion() {
		
		List<SubjectSuggestion> newSuggestions = new ArrayList<SubjectSuggestion>();
		
		if (subjectSuggestions != null && subjectSuggestion != null) {
			LevenshteinDistance l = LevenshteinDistance.getDefaultInstance();
			for (SubjectSuggestion s : subjectSuggestions) {		
				if (l.apply(s.subject,subjectSuggestion) > 4) 
				{
					newSuggestions.add(s);
				}
			}
		}
		
		subjectSuggestions = newSuggestions;
	}

	public String clearSubjectSuggestion() {
		subjectSuggestions = null;
		subjects.setSuggestion(null);
		subjectSuggestion = "";
		return null;
	}
	
	private void calculateSubjectSuggestions() {
		
		try {
			subjectSuggestions = new ArrayList<SubjectSuggestion>();
			List<FileSummaryDTO> allSubjects = dataService.getFilesGroupBy(-1, 0, null, "asunto", false, "asunto");
			LevenshteinDistance l = LevenshteinDistance.getDefaultInstance();
			
			int index = 0;
			while (index < allSubjects.size()) {
				String s = allSubjects.get(index).getAsunto().trim().toLowerCase().replaceAll("/á/é/í/ó/í","_").replaceAll("\\s+","%");
				SubjectSuggestion subjectS = new SubjectSuggestion();
				subjectS.count = 0;
				subjectS.subject = s;
				for (int index2 = index; index2 < allSubjects.size(); index2++) {		
					String s2 = allSubjects.get(index2).getAsunto().trim().toLowerCase().replaceAll("/á/é/í/ó/í","_").replaceAll("\\s+","%");
					int distance = l.apply(s, s2);
					if (distance < 3) {
						subjectS.count++;
					}
				}
				subjectSuggestions.add(subjectS);
				
				index++;
			}
			
		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
	}

	public String suggestClient() {
		
		try {
			
			if (clientSuggestion == null)
				clientSuggestion = dataService.getDuplicateClients();
			
			if(clientSuggestion != null && clientSuggestion.size() > 0) {
				int index = 0;
				Integer suggestClient = lastClientSuggestion;
				Integer suggestClient2 = lastClientSuggestion;
				while (suggestClient.intValue() <= lastClientSuggestion.intValue()) {
					suggestClient = clientSuggestion.get(index)[0];
					suggestClient2 = clientSuggestion.get(index)[1];
					index++;
				}
				
				lastClientSuggestion = suggestClient;	
				clientsDataModel1.setId(suggestClient);
				clientsDataModel2.setId(suggestClient2);
			}
			
			
		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
		
		return null;
	}
	
	public String clearClientSuggestion() {
		clientSuggestion = null;
		lastClientSuggestion = new Integer(-1);
		clientsDataModel1.setId(null);
		clientsDataModel2.setId(null);
		return null;
	}
	
	public String unifySubjects() {
		
		if (selectedSubjects != null && selectedSubjects.size() > 0 && newSubject != null && newSubject.length() > 0) {
			
			try {
				List<String> theSubjects = new ArrayList<String>();
				for (FileSummaryDTO file : selectedSubjects) {
					theSubjects.add(file.getAsunto());
				}
				dataService.unifySubjects(theSubjects, newSubject.trim());	
				
				selectedSubjects = null;
				newSubject = "";
			} catch (Exception e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();						
			}
		}
		else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Debe seleccionar más de un asunto y elegir un nuevo nombre"));
			context.validationFailed();			
		}
		
		return null;
	}
	
	public void unifyClients() {
		
		if (selectedClient1 != null && selectedClient2 != null && selectedClient1.getId().intValue() != selectedClient2.getId().intValue()) {
			
			try {
				dataService.unifyClients(selectedClient1.getId().intValue(), selectedClient2.getId().intValue());	

				selectedClient1 = null;
				selectedClient2 = null;

			} catch (Exception e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();						
			}			
		}
		else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Debe seleccionar un cliente de cada tabla"));
			context.validationFailed();			
		}		
	}
}

class SubjectSuggestion {
	int count = 0;
	String subject = "";
}

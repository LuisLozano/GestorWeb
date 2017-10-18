package es.salazaryasociados.gestorui.view.configuration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.services.data.api.IDataService;
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
	private SubjectsDataModel subjects;
	
	@Getter @Setter
	private List<String> selectedSubjects;
	
	@Getter @Setter
	private String newSubject;
	
	@Inject
	private IDataService dataService;
	
	@PostConstruct
	public void init() {
		super.init();
		
		subjects = new SubjectsDataModel(dataService);
		initData();
	}
	
	private void initData() {
	}	
	
	public String unify() {
		
		if (selectedSubjects != null && selectedSubjects.size() > 0 && newSubject != null && newSubject.length() > 0) {
			
			try {
				dataService.unify(selectedSubjects, newSubject.trim());	
				
				selectedSubjects = null;
				newSubject = "";
			} catch (Exception e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();						
			}
		}
		
		return null;
	}
}

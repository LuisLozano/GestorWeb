package es.salazaryasociados.gestorui.view.expedientes;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.service.IDataService;

@ManagedBean
@SessionScoped
public class ExpedientesBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ExpedientesDataModel expedientesModel;
	private List<Responsable> responsables;
	private IDataService dataService = null; 
	
	private Integer selectedFile;
	
	@PostConstruct
    public void init() {
		dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		expedientesModel = new ExpedientesDataModel();
		try {
			responsables = dataService.getAllResponsables(-1, 0, null, null, false);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
    }
	
	public ExpedientesDataModel getExpedientesModel() {
		return expedientesModel;
	}

	public void setExpedientesModel(ExpedientesDataModel expedientesModel) {
		this.expedientesModel = expedientesModel;
	}

	public List<Responsable> getResponsables() {
		return responsables;
	}

	public void setResponsables(List<Responsable> responsables) {
		this.responsables = responsables;
	}

	public Integer getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(Integer selectedFile) {
		this.selectedFile = selectedFile;
	}	
	
	public String showFileDetail(Integer selectedFile){
		setSelectedFile(selectedFile);
		return "showFile?faces-redirect=true";
	}
	
	public String newFile(){
		setSelectedFile(null);
		return "showFile?faces-redirect=true";
	}
	
	public void deleteFile() {
		try {
			dataService.deleteExpediente(selectedFile);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
	}
}

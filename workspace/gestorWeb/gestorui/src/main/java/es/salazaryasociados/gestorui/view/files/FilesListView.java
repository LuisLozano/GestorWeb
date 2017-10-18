package es.salazaryasociados.gestorui.view.files;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class FilesListView extends SalazarBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3597994908267954440L;

	@Inject
	protected IDataService dataService;
	@Getter
	@Setter
	private LazyDataModel<FileSummaryDTO> dataModel;
	@Getter
	@Setter
	private Integer selectedFile;
	
	@PostConstruct
	public void init() {
		super.init();
		dataModel = new FilesDataModel(dataService);
	}
	
	public void newFile() {
		selectedFile = null;
	}
	
	public void deleteFile() {
		
		if (selectedFile != null) {
			try {
				dataService.deleteFile(selectedFile);
			} catch (DataServiceException e) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();				
			}
		}
		
		selectedFile = null;
	}
}



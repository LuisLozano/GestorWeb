package es.salazaryasociados.gestorui.view.responsables;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.service.IDataService;

@ManagedBean
@ViewScoped
public class ResponsablesBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4225083007917397713L;
	private IDataService dataService;
	private FilesDataModel filesDataModel;
	private Integer selectedResponsableID;
	private Responsable selectedResponsable;
	private List<Responsable> responsables;
	private boolean includeClosed = false;
	private boolean includeSecondResp = false;
	
	@PostConstruct
    public void init() {
		dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		try {
			setResponsables(dataService.getAllResponsables(-1, 0, null, null, true));
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFilesDataModel(new FilesDataModel(this));
    }

	public FilesDataModel getFilesDataModel() {
		return filesDataModel;
	}

	public void setFilesDataModel(FilesDataModel dataModel) {
		this.filesDataModel = dataModel;
	}

	public void deleteResponsable(){
	}
	
	public void newResponsable(ActionEvent actionEvent){
		selectedResponsable = new Responsable();
	}
	
	public Integer getSelectedResponsableID() {
		return selectedResponsableID;
	}
	
	public Responsable getSelectedResponsable() {
		return selectedResponsable;
	}
	
	public void setSelectedResponsable(Responsable r) {
		selectedResponsable = r;
	}
	
	public void setSelectedResponsableID(Integer sc) {
		this.selectedResponsableID = sc;
		try {
			selectedResponsable = dataService.getResponsableById(selectedResponsableID);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}
	}
	
	public void save() {
		try {
			dataService.saveResponsable(selectedResponsable);
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );
		}		
	}
	
	public boolean filterByPesupuesto(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
            
        char comparator = filterText.charAt(0);
        if (filterText.startsWith(">") || filterText.startsWith("<")) {
        	filterText = filterText.substring(1).trim();
        }
        
        if (comparator == '>')
        	return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
        else if (comparator == '<')
           	return ((Comparable) value).compareTo(Integer.valueOf(filterText)) < 0;
        else
        	return ((Comparable) value).compareTo(Integer.valueOf(filterText)) == 0;
    }	
	
    public void onRowEdit(RowEditEvent event) {
    	System.out.println("Llega");
    }
     
    public void onRowCancel(RowEditEvent event) {
    	System.out.println("Llega");
    }	

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Car Selected" + ((Responsable) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

	public List<Responsable> getResponsables() {
		return responsables;
	}

	public void setResponsables(List<Responsable> responsables) {
		this.responsables = responsables;
	}

	public boolean getIncludeClosed() {
		return includeClosed;
	}

	public void setIncludeClosed(boolean includeClosed) {
		this.includeClosed = includeClosed;
	}

	public boolean getIncludeSecondResp() {
		return includeSecondResp;
	}

	public void setIncludeSecondResp(boolean includeSecondResp) {
		this.includeSecondResp = includeSecondResp;
	}    
}

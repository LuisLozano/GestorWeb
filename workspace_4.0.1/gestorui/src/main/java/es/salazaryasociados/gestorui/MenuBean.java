package es.salazaryasociados.gestorui;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.menu.MenuModel;


@ManagedBean
@SessionScoped
public class MenuBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9151282139757562595L;
	public boolean blocked = false;
	public MenuModel getMainMenu() {
		return MenuUtils.GetMenu();
	}

	@PostConstruct
	public void init(){
		MenuUtils.Clear();
		MenuUtils.AddMenu("Expedientes", "/expedientes.xhtml");
		MenuUtils.AddMenu("Clientes", "/clientes.xhtml");
		MenuUtils.AddMenu("Responsables", "/responsables.xhtml");
		MenuUtils.AddMenu("Configuración>Temas", "/configuracion.xhtml");
	}
	
	public void idleListener() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("sessionLost.xhtml");
    }	
}
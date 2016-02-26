package es.salazaryasociados.gestorui.extension.view.configuration;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import es.salazaryasociados.gestorui.MenuUtils;

@SuppressWarnings("restriction")
@ManagedBean(eager=true, name="extensionMenu")
@ApplicationScoped
public class ExtensionMenu {

	@PostConstruct
	public void init(){

		MenuUtils.AddMenu("Configuración>Administración", "/admin/admin.xhtml");
	}	
}

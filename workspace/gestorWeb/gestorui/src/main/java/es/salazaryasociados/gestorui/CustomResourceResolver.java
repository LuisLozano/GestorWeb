package es.salazaryasociados.gestorui;

import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ResourceResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que permite en desarrollo probar las ventanas
 * @author luis.lozano
 *
 */
public class CustomResourceResolver extends ResourceResolver {

	protected Logger logger = LoggerFactory.getLogger(CustomResourceResolver.class);
	private static final String PAGE_SUFIX = ".xhtml";
	private ResourceResolver parent;
	private String basePath;

    public CustomResourceResolver(ResourceResolver theParent) {
    	parent = theParent;
        this.basePath = "E:/Desarrollo/projects/SalazarYAsociados/yaroki/workspace/gestorWeb/gestorui/src/main/webapp";
    }
    
	@Override
	public URL resolveUrl(String path) {
    	URL url = null;
    	if (path.endsWith(PAGE_SUFIX))// && !path.contains("WEB-INF"))
    	{
	    	try {
				url = new URL("file", "", basePath + path);
			} catch (MalformedURLException e) {
				logger.error(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Consulte con el administrador"));
			}    	    		
    	}
    	else
    	{
    		url = parent.resolveUrl(path); // Resolves from WAR (el war tiene prioridad).
    	}
        return url;
	}

}

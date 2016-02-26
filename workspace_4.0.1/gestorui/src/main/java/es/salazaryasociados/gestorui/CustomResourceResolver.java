package es.salazaryasociados.gestorui;

import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

public class CustomResourceResolver extends ResourceResolver {

	private static final String PAGE_SUFIX = ".xhtml";
	private ResourceResolver parent;
	private String basePath;

    public CustomResourceResolver(ResourceResolver theParent) {
    	parent = theParent;
        this.basePath = "./llaroqui";
    }

    public URL resolveUrl(String path) {
    	
    	URL url = null;
    	System.out.println("Busca la url para " + path);
    	if (path.endsWith(PAGE_SUFIX) && !path.contains("WEB-INF"))
    	{
	    	try {
				url = new URL("file", "", basePath + path);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	    		
    	}
    	else
    	{
    		url = parent.resolveUrl(path); // Resolves from WAR (el war tiene prioridad).
    		System.out.println("Va al padre");
    	}
        return url;
    }
}

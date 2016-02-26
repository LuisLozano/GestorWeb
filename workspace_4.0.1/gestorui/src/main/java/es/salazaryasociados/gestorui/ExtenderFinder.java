package es.salazaryasociados.gestorui;

import java.beans.Introspector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.myfaces.config.ManagedBeanBuilder;
import org.apache.myfaces.config.RuntimeConfig;
import org.apache.myfaces.config.impl.digester.elements.ManagedBeanImpl;

/**
 * Esta clase permite a�adir todas las extensiones al framework
 * @author luis.lozano
 *
 */

@ManagedBean(eager=true)//eager=true permite que se llame a @PostConstruct al crear la aplicaci�n
@ApplicationScoped
public class ExtenderFinder {

	/**
	 * Constante que indica como deben llamarse los ficheros considerados extensiones
	 */
	public final static String EXT_CONS = "gestorui";
	
	@PostConstruct
	public void init() {
		buildExtensions();
	}
	

    /**
     * Busca entre las extensiones, todos los bean de jsf
     */
    private void buildExtensions() {
    	File deployDir = new File("./deploy");
    	// Se buscan en el directorio deploy todos los ficheros que encajen en el patr�n:
    	//EXT_CONS.extension_name.extension_version.jar
    	if (deployDir != null && deployDir.isDirectory())
    	{
    		FilenameFilter filter = new FilenameFilter() {
    		    @Override
    		    public boolean accept(File dir, String name) {
    		        return name.matches(EXT_CONS + "(.*).jar");
    		    }
    		};
    		
    		File[] extensions = deployDir.listFiles(filter);
    		buildExtensions(extensions);
    	}
	}

    /**
     * A�ade las extensiones encontradas
     * @param extensions Ficheros jar que contienen las extensiones
     */
	private void buildExtensions(File[] extensions) {
		if (extensions != null && extensions.length > 0)
		{
			for (File extension : extensions){
				List<Class<?>> classes = getClassesFromExtension(extension);
				registerClasses(classes);
			}
		}
	}

	/**
	 * Busca las clases que hay en un fichero jar
	 * @param file Fichero jar
	 * @return
	 */
	private List<Class<?>> getClassesFromExtension(File file) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		
		if (file != null)
		{
			List<String> classNames = new ArrayList<String>();
			try{			
				ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
				for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
				{
				    if (!entry.isDirectory() && entry.getName().endsWith(".class")) 
				    {
				        // This ZipEntry represents a class. Now, what class does it represent?
				        String className = entry.getName().replace('/', '.'); // including ".class"
				        classNames.add(className.substring(0, className.length() - ".class".length()));
				    }
				}
				zip.close();
			}catch(FileNotFoundException nfoundE){
				;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if (classNames.size() > 0)
			{
				for (String className : classNames) {
					
					try {
						Class<?> c = Class.forName(className);
						classes.add(c);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}
			
		}
		
		return classes;
	}

	private void registerClasses(List<Class<?>> classes) {
		if (classes != null)
		{
			for (Class<?> c : classes)
			{
				registerClass(c);
			}
		}
	}

	private void registerClass(Class<?> c) {
		boolean isAnnotated = false;
		
		ManagedBean managedAnnotation = c.getAnnotation(javax.faces.bean.ManagedBean.class);		
		isAnnotated = (managedAnnotation != null);
		
		if (isAnnotated)
		{
			String name = managedAnnotation.name();
			
			if (name.trim().length() <= 0)
			{
				name = Introspector.decapitalize(c.getSimpleName());
			}			
			
			//org.apache.myfaces.config.impl.digester.elements.ManagedBean theBean = new org.apache.myfaces.config.impl.digester.elements.ManagedBean();
			ManagedBeanImpl theBean = new ManagedBeanImpl();
			theBean.setBeanClass(c.getName());
			theBean.setName(name);
			
			String scope = getScope(c);
			theBean.setScope(scope);
			
			theBean.setEager(managedAnnotation.eager() ? "true" : "false");

			FacesContext ctx = FacesContext.getCurrentInstance();
			ExternalContext externalContext = ctx.getExternalContext();

			RuntimeConfig runtimeConfig = RuntimeConfig
					.getCurrentInstance(externalContext);
			runtimeConfig.addManagedBean(name, theBean);

			// Se construyen los applicationscoped bean con eager = true
			if ("application".equals(scope) && "true".equals(theBean.getEager()))
			{
	            ManagedBeanBuilder managedBeanBuilder = new ManagedBeanBuilder();
	            Map<String, Object> applicationMap = externalContext.getApplicationMap();
	            
                // create instance
                Object beanInstance = managedBeanBuilder.buildManagedBean(ctx, theBean);
                
                // put in application scope
                applicationMap.put(theBean.getManagedBeanName(), beanInstance);	            
			}
		}
	}

	/**
	 * Devuelve el scope de una clase
	 * @param c
	 * @return
	 */
	private String getScope(Class<?> c) {
		
		String scope = "request";
		if ((c.getAnnotation(javax.faces.bean.SessionScoped.class) != null))
		{
			scope = "session";
		}
		else if ((c.getAnnotation(javax.faces.bean.ApplicationScoped.class) != null))
		{
			scope = "application";
		}
		else if ((c.getAnnotation(javax.faces.bean.NoneScoped.class) != null))
		{
			scope = "none";
		}
		else if ((c.getAnnotation(javax.faces.bean.RequestScoped.class) != null))
		{
			scope = "request";
		}				
		else if ((c.getAnnotation(javax.faces.bean.ViewScoped.class) != null))
		{
			scope = "view";
		}	
		
		return scope;
	}	
}

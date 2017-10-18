package es.salazaryasociados.gestorui;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ServicesProvider {

	/**
	 * Instancia
	 */
	private static ServicesProvider This;
	/**
	 * Contexto para obtener los servicios
	 */
	private BundleContext context;
	
	/**
	 * Establece el contexto para poder obtener los servicios
	 * @param context
	 */
	public static void SetContext(BundleContext context) {
		GetInstance().context = context;
	}

	/**
	 * Constructor privado, se trata de un singleton
	 */
	private ServicesProvider(){
	}

	/**
	 * Obtiene la instancia
	 * @return
	 */
	public static ServicesProvider GetInstance(){
		if (This == null){
			This = new ServicesProvider();
		}
		return This;
	}
	
	/**
	 * Obtiene un servicio a partir del nombre de la clase que provee
	 */
	public static <S> S GetService(Class<S> clazz) {
		S result = null;
		if (GetInstance().context != null)
		{
			try{
				ServiceReference<S> ref = (ServiceReference<S>) This.context.getServiceReference(clazz);
				if (ref != null)
				{
					result = This.context.getService(ref);
				}
			}catch(Exception e){
				//TODO: log
			}
		}
		else {
			throw new NullPointerException("No se ha establecido el contexto. Esto debe hacerse en el activador");
		}
		return result;
	}
}

package es.salazaryasociados.services.data.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;

import es.salazaryasociados.services.mail.MailJobSrv;
public class DataServiceActivator implements BundleActivator {

	@SuppressWarnings("rawtypes")
	private ServiceRegistration ppcService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(BundleContext context) throws Exception {
		
		Dictionary props = new Hashtable();
	    props.put("service.pid", "es.salazaryasociados.MailJob");
	    ppcService = context.registerService(ManagedService.class.getName(),
	        new MailJobSrv(context), props);						
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (ppcService != null) {
	        ppcService.unregister();
	        ppcService = null;
	    }
	}

}

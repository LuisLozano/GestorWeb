package es.salazaryasociados.services.mail;

import java.util.Dictionary;

import org.apache.karaf.scheduler.Job;
import org.apache.karaf.scheduler.ScheduleOptions;
import org.apache.karaf.scheduler.Scheduler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailJobSrv implements ManagedService{

	private BundleContext context;

	private static Logger logger = LoggerFactory.getLogger(MailJobSrv.class);
	
	public MailJobSrv(BundleContext context) {
		this.context = context;
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		String cron = (String)properties.get("cron");
		
		Scheduler scheduler = getScheduler();
		if (scheduler != null) {
			
			ScheduleOptions options = scheduler .EXPR(cron);
			options.name("mailJob");
			
			boolean removed = scheduler.unschedule(options.name());
			Job job = new MailJob(properties, context);
			try {
				scheduler.schedule(job, options);
			} catch (IllegalArgumentException e) {
				logger.error("Error al actualizar la configuración del servicio de correo", e);
			} catch (SchedulerException e) {
				logger.error("Error al actualizar la configuración del servicio de correo", e);
			}
		}
	}

	private Scheduler getScheduler() {
		ServiceReference<org.apache.karaf.scheduler.Scheduler> reference = (ServiceReference<org.apache.karaf.scheduler.Scheduler>) context.getServiceReference(org.apache.karaf.scheduler.Scheduler.class);
		Scheduler scheduler = context.getService(reference);

		return scheduler;
	}

}

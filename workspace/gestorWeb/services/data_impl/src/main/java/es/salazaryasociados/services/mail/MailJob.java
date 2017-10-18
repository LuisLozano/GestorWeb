package es.salazaryasociados.services.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.karaf.scheduler.Job;
import org.apache.karaf.scheduler.JobContext;
import org.joda.time.DateTime;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.dto.EventDTO;
import es.salazaryasociados.services.data.dto.FileDTO;

public class MailJob implements Job {

	private String user;
	private String password;
	private BundleContext context;
	private String mail_smtp_host;
	private String mail_smtp_port;
	private String fromAddress;
	
	private static Logger logger = LoggerFactory.getLogger(MailJob.class);

	public MailJob(Dictionary<String, ?> properties, BundleContext context) {

		this.user = (String) properties.get("user");
		this.password = (String) properties.get("password");
		this.mail_smtp_host = (String)properties.get("mail.smtp.host");
		this.mail_smtp_port = (String)properties.get("mail.smtp.port");
		this.fromAddress = (String)properties.get("fromAddress");
		this.context = context;
	}

	@Override
	public void execute(JobContext context) {

		Date dt = new Date();
		DateTime dtOrg = new DateTime(dt);
		DateTime dtPlusOneStart = dtOrg.plusDays(1).withTimeAtStartOfDay();
		DateTime dtPlusOneEnd = dtPlusOneStart.withTime(23, 59, 59, 999);

		logger.info("Enviando mail " + dtPlusOneStart.toString() + " - " + dtPlusOneEnd.toString());
		IDataService dataService = getDataService();

		try {
			List<EventDTO> events = dataService.getAllEvents(dtPlusOneStart.toDate(), dtPlusOneEnd.toDate());
			if (events != null && events.size() > 0) {
				for (EventDTO event : events) {

					List<String> tos = new ArrayList<String>();

					if (event.getEnviarMailClientes() || event.getEnviarMailResp()) {
						FileDTO file = dataService.getFileById(event.getFile().getId());
						if (event.getEnviarMailResp()) {
							// Solo se envia al responsable principal
							tos.add(file.getResponsible1().getEmail());
						}
						if (event.getEnviarMailClientes()) {
							for (ClientSummaryDTO c : file.getClients()) {
								ClientDTO client = dataService.getClientById(c.getId());
								String mail = client.getCorreoElectronico();
								if (mail != null)
									tos.add(mail);
							}
						}
					}

					mailTo(tos, event);
				}
			}
		} catch (Exception e) {
			logger.error("Error al intentar enviar correos", e);
		}
	}

	private void mailTo(List<String> tos, EventDTO event) {

		logger.debug("Mandando a");
		for (String mail : tos) {			
			logger.debug(mail);
		}

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", mail_smtp_host);
		props.put("mail.smtp.port", mail_smtp_port);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {

			String toAddresses = "";
			int i = 0;
			for (String toAddress : tos) {
				toAddresses += toAddress;
				i++;
				if (i < tos.size()) {
					toAddresses +=",";
				}
			}
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddresses));
			message.setSubject("Recordatorio de evento: " + event.getTitulo());
			String text = "Estimado cliente,\n\n";
			text += "Le recordamos que el proximo " + event.getFechaInicio().toString() + " tendrá lugar el evento cuyos datos se adjuntan:\n\n";
			text += "Título: " + event.getTitulo() + "\n\n";
			text += "Observaciones:\n\n";
			text += event.getObservaciones();
			message.setText(text);

			Transport.send(message);

			logger.debug("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private IDataService getDataService() {
		ServiceReference<IDataService> reference = (ServiceReference<IDataService>) context
				.getServiceReference(IDataService.class);
		IDataService dataService = context.getService(reference);

		return dataService;
	}
}

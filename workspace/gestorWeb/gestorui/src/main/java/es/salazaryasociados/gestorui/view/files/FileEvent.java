package es.salazaryasociados.gestorui.view.files;

import java.sql.Timestamp;
import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;

import es.salazaryasociados.services.data.dto.EventDTO;
import lombok.Getter;
import lombok.Setter;

public class FileEvent extends DefaultScheduleEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8244257055411575453L;
	
	@Getter @Setter
	private EventDTO event;
	
	public FileEvent (String title, Date start, Date end) {
		super(title, start, end);
		event = new EventDTO();
	}
	
	public FileEvent(EventDTO eventDTO) {
		super(eventDTO.getTitulo(), new Date(eventDTO.getFechaInicio().getTime()), new Date(eventDTO.getFechaFin().getTime()));
		setDescription(eventDTO.getObservaciones());
		setEvent(eventDTO);
		setId(eventDTO.getId().toString());
	}

	public void setSendMailToClients(boolean send) {
		event.setEnviarMailClientes(send);
	}
	
	public void setSendMailToResponsibles(boolean send) {
		event.setEnviarMailResp(send);
	}	
	
	public boolean getSendMailToClients() {
		return event.getEnviarMailClientes();
	}
	
	public boolean getSendMailToResponsibles() {
		return event.getEnviarMailResp();
	}	
	
	public EventDTO buildEventDTO() {
		event.setFechaFin(new Timestamp(this.getEndDate().getTime()));
		event.setFechaInicio(new Timestamp(this.getStartDate().getTime()));
		event.setTitulo(this.getTitle());
		event.setObservaciones(this.getDescription());
		return event;
	}
}

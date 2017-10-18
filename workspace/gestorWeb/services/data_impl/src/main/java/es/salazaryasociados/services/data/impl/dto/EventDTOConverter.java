package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Evento;
import es.salazaryasociados.services.data.dto.EventDTO;
import lombok.Setter;

public class EventDTOConverter extends EntityToDTO<Evento, EventDTO> {

	@Setter
	private FileSummaryDTOConverter fileConverter;
	
	public EventDTOConverter(Class<Evento> entityClass, Class<EventDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Evento createEntity() {
		return new Evento();
	}

	@Override
	public EventDTO createDTO() {
		return new EventDTO();
	}

	public EventDTO getDto(Evento entity) {
		EventDTO result = super.getDto(entity);
		
		//Obtener Expediente
		if (entity.getExpediente() != null) {
			result.setFile(fileConverter.getDto(entity.getExpediente()));
		}

		return result;
	}
	
	public Evento getEntity(EventDTO dto) {
		Evento result = super.getEntity(dto);
		
		//Obtener Expediente
		if (dto.getFile() != null) {
			result.setExpediente(fileConverter.getEntity(dto.getFile()));
		}
		return result;
	}
}

package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Responsable;
import es.salazaryasociados.services.data.dto.ResponsibleDTO;
import lombok.Setter;

public class ResponsibleDTOConverter extends EntityToDTO<Responsable, ResponsibleDTO> {

	@Setter
	PaymentSummaryDTOConverter paymentSummaryConverter;
	
	public ResponsibleDTOConverter(Class<Responsable> entityClass, Class<ResponsibleDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Responsable createEntity() {
		return new Responsable();
	}

	@Override
	public ResponsibleDTO createDTO() {
		return new ResponsibleDTO();
	}
}

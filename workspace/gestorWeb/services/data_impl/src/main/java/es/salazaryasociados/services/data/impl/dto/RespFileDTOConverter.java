package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.ListadoExpResp;
import es.salazaryasociados.services.data.dto.ResponsibleFilesDTO;
import lombok.Setter;

public class RespFileDTOConverter extends EntityToDTO<ListadoExpResp, ResponsibleFilesDTO> {

	@Setter
	private ResponsibleDTOConverter responsibleConverter;

	public RespFileDTOConverter(Class<ListadoExpResp> entityClass, Class<ResponsibleFilesDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public ListadoExpResp createEntity() {
		return new ListadoExpResp();
	}

	@Override
	public ResponsibleFilesDTO createDTO() {
		return new ResponsibleFilesDTO();
	}
	
	public ResponsibleFilesDTO getDto(ListadoExpResp entity) {
		ResponsibleFilesDTO result = super.getDto(entity);	
		
		//Obtener responsables:
		if (entity.getResponsable1() != null) {
			result.setResponsible1(responsibleConverter.getDto(entity.getResponsable1()));
		}

		if (entity.getResponsable2() != null) {
			result.setResponsible2(responsibleConverter.getDto(entity.getResponsable2()));
		}
		
		return result;
	}
}

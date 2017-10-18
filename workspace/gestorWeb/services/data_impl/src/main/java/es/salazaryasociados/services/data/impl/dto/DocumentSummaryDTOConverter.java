package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Documento;
import es.salazaryasociados.services.data.dto.DocumentSummaryDTO;
import lombok.Setter;

public class DocumentSummaryDTOConverter extends EntityToDTO<Documento, DocumentSummaryDTO> {

	@Setter
	private FileSummaryDTOConverter fileConverter;

	public DocumentSummaryDTOConverter(Class<Documento> entityClass, Class<DocumentSummaryDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Documento createEntity() {
		return new Documento();
	}

	@Override
	public DocumentSummaryDTO createDTO() {
		return new DocumentSummaryDTO();
	}	
}

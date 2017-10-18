package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Cliente;
import es.salazaryasociados.model.Documento;
import es.salazaryasociados.model.Expediente;
import es.salazaryasociados.services.data.dto.DocumentDTO;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;
import lombok.Setter;

public class DocumentDTOConverter extends EntityToDTO<Documento, DocumentDTO> {

	@Setter
	private FileSummaryDTOConverter fileConverter;

	public DocumentDTOConverter(Class<Documento> entityClass, Class<DocumentDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Documento createEntity() {
		return new Documento();
	}

	@Override
	public DocumentDTO createDTO() {
		return new DocumentDTO();
	}
	
	public DocumentDTO getDto(Documento entity) {
		DocumentDTO result = super.getDto(entity);
		
		// Se obtiene el expediente
		FileSummaryDTO file = fileConverter.getDto(entity.getExpediente());
		result.setFile(file);
		
		return result;
	}
	
	public Documento getEntity(DocumentDTO dto) {
		Documento result = super.getEntity(dto);
				
		// Se obtiene el expediente		
		if (dto.getFile() != null)
		{
			Expediente e = new Expediente();
			e.setId(dto.getFile().getId());
			result.setExpediente(e);
		}		
		
		// Se obtiene el cliente
		if (dto.getClient() != null)
		{
			Cliente c = new Cliente();
			c.setId(dto.getClient().getId());
			result.setCliente(c);
		}		
		
		return result;
	}	
}

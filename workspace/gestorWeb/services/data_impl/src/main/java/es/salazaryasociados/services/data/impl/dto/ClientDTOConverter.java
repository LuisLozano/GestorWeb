package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Cliente;
import es.salazaryasociados.model.Documento;
import es.salazaryasociados.model.Expediente;
import es.salazaryasociados.services.data.dto.ClientDTO;
import lombok.Setter;

public class ClientDTOConverter extends EntityToDTO<Cliente, ClientDTO> {

	@Setter
	private FileSummaryDTOConverter fileConverter;
	@Setter
	DocumentSummaryDTOConverter documentSummaryConverter;
	
	public ClientDTOConverter(Class<Cliente> entityClass, Class<ClientDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Cliente createEntity() {
		return new Cliente();
	}

	@Override
	public ClientDTO createDTO() {
		return new ClientDTO();
	}
	
	public ClientDTO getDto(Cliente entity) {
		ClientDTO result = super.getDto(entity);
		
		//Se obtienen los expedientes:
		if (entity.getExpedientes() != null && entity.getExpedientes().size() > 0)
		{
			for (Expediente exp : entity.getExpedientes()) {
				result.getFiles().add(fileConverter.getDto(exp));
			}
		}
		
		//Obtener documentos:
		if (entity.getDocumentos() != null) {
			for (Documento document : entity.getDocumentos()) {
				result.getDocuments().add(documentSummaryConverter.getDto(document));
			}
		}		
		return result;
	}
	
	public Cliente getEntity(ClientDTO dto) {
		Cliente result = super.getEntity(dto);
		return result;
	}	
}

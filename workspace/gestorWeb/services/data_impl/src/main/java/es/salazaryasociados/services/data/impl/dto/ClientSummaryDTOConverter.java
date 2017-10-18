package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Cliente;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;

public class ClientSummaryDTOConverter extends EntityToDTO<Cliente, ClientSummaryDTO> {

	public ClientSummaryDTOConverter(Class<Cliente> entityClass, Class<ClientSummaryDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Cliente createEntity() {
		return new Cliente();
	}

	@Override
	public ClientSummaryDTO createDTO() {
		return new ClientSummaryDTO();
	}
	
	public ClientSummaryDTO getDto(Cliente entity) {
		ClientSummaryDTO result = super.getDto(entity);		
		return result;
	}
}

package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Cliente;
import es.salazaryasociados.model.Documento;
import es.salazaryasociados.model.Evento;
import es.salazaryasociados.model.Expediente;
import es.salazaryasociados.model.Pago;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.dto.FileDTO;
import lombok.Setter;

public class FileDTOConverter extends EntityToDTO<Expediente, FileDTO> {

	@Setter
	PaymentSummaryDTOConverter paymentSummaryConverter;
	@Setter
	EventDTOConverter eventDTOConverter;
	@Setter
	ResponsibleDTOConverter responsibleConverter;
	@Setter
	ClientSummaryDTOConverter clientSummaryConverter;
	@Setter
	DocumentSummaryDTOConverter documentSummaryConverter;
	
	public FileDTOConverter(Class<Expediente> entityClass, Class<FileDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Expediente createEntity() {
		return new Expediente();
	}

	@Override
	public FileDTO createDTO() {
		return new FileDTO();
	}

	public FileDTO getDto(Expediente entity) {
		FileDTO result = super.getDto(entity);
		
		//Obtener pagos:
		if (entity.getPagos() != null) {
			for (Pago pago : entity.getPagos()) {
				result.getPayments().add(paymentSummaryConverter.getDto(pago));
			}
		}
		
		//Obtener eventos:
		if (entity.getEventos() != null) {
			for (Evento evento : entity.getEventos()) {
				result.getEvents().add(eventDTOConverter.getDto(evento));
			}
		}		
		
		//Obtener responsables:
		if (entity.getResponsable1() != null) {
			result.setResponsible1(responsibleConverter.getDto(entity.getResponsable1()));
		}

		if (entity.getResponsable2() != null) {
			result.setResponsible2(responsibleConverter.getDto(entity.getResponsable2()));
		}		

		//Obtener clientes:
		if (entity.getClientes() != null) {
			for (Cliente client : entity.getClientes()) {
				result.getClients().add(clientSummaryConverter.getDto(client));
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
	
	public Expediente getEntity(FileDTO dto) {
		Expediente result = super.getEntity(dto);
		
		//Obtener responsables:
		if (dto.getResponsible1() != null) {
			result.setResponsable1(responsibleConverter.getEntity(dto.getResponsible1()));
		}

		if (dto.getResponsible2() != null) {
			result.setResponsable2(responsibleConverter.getEntity(dto.getResponsible2()));
		}
		
		//Obtener clientes:
		if (dto.getClients() != null) {
			for (ClientSummaryDTO client : dto.getClients()) {
				result.getClientes().add(clientSummaryConverter.getEntity(client));
			}
		}				
		
		return result;
	}
}

package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Pago;
import es.salazaryasociados.services.data.dto.PaymentDTO;
import lombok.Setter;

public class PaymentDTOConverter extends EntityToDTO<Pago, PaymentDTO> {

	@Setter
	private FileSummaryDTOConverter fileConverter;
	@Setter
	private ClientSummaryDTOConverter clientSummaryConverter;
	
	public PaymentDTOConverter(Class<Pago> entityClass, Class<PaymentDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Pago createEntity() {
		return new Pago();
	}

	@Override
	public PaymentDTO createDTO() {
		return new PaymentDTO();
	}

	public PaymentDTO getDto(Pago entity) {
		PaymentDTO result = super.getDto(entity);
		
		//Obtener Expediente
		if (entity.getExpediente() != null) {
			result.setFile(fileConverter.getDto(entity.getExpediente()));
		}

		//Obtener clientes:
		if (entity.getPagador() != null) {
			result.setPayer(clientSummaryConverter.getDto(entity.getPagador()));
		}		
		
		return result;
	}
	
	public Pago getEntity(PaymentDTO dto) {
		Pago result = super.getEntity(dto);
		
		//Obtener Expediente
		if (dto.getFile() != null) {
			result.setExpediente(fileConverter.getEntity(dto.getFile()));
		}

		//Obtener clientes:
		if (dto.getPayer() != null) {
			result.setPagador(clientSummaryConverter.getEntity(dto.getPayer()));
		}		
		
		return result;
	}
}

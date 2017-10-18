package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Pago;
import es.salazaryasociados.services.data.dto.PaymentSummaryDTO;

public class PaymentSummaryDTOConverter extends EntityToDTO<Pago, PaymentSummaryDTO> {

	public PaymentSummaryDTOConverter(Class<Pago> entityClass, Class<PaymentSummaryDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Pago createEntity() {
		return new Pago();
	}

	@Override
	public PaymentSummaryDTO createDTO() {
		return new PaymentSummaryDTO();
	}
	
	public PaymentSummaryDTO getDto(Pago entity) {
		PaymentSummaryDTO result = super.getDto(entity);
		
		result.setFile(entity.getExpediente().getId());
		result.setPayer(entity.getPagador().getApellidos() + ", " + entity.getPagador().getNombre());
		return result;
	}
}

package es.salazaryasociados.services.data.impl.dto;

import es.salazaryasociados.model.Expediente;
import es.salazaryasociados.model.Pago;
import es.salazaryasociados.services.data.dto.FileSummaryDTO;

public class FileSummaryDTOConverter extends EntityToDTO<Expediente, FileSummaryDTO> {

	public FileSummaryDTOConverter(Class<Expediente> entityClass, Class<FileSummaryDTO> dtoClass) {
		super(entityClass, dtoClass);
	}

	@Override
	public Expediente createEntity() {
		return new Expediente();
	}

	@Override
	public FileSummaryDTO createDTO() {
		return new FileSummaryDTO();
	}
	
	public FileSummaryDTO getDto(Expediente entity) {
		FileSummaryDTO result = super.getDto(entity);
		
		//Obtener pagado:
		if (entity.getPagos() != null) {
			for (Pago pago : entity.getPagos()) {
				result.setPagado(result.getPagado().add(pago.getCantidad()));
			}
		}
		
		//Obtener responsable:
		if (entity.getResponsable1() != null) {
			result.setResponsable(entity.getResponsable1().getNombre());
		}

		// Obtener moroso:
		result.setMoroso(result.getPresupuesto().subtract(result.getPagado()));
		return result;
	}
}

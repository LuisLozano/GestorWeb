package es.salazaryasociados.gestorui.view.responsibles;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ResponsibleDTO;
import es.salazaryasociados.services.data.dto.ResponsibleFilesDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class JRResponsibleFilesDS implements JRDataSource {

	private static final String EXCEPTION_MESSAGE_KEY_BEAN_FIELD_VALUE_NOT_RETRIEVED = "data.bean.field.value.not.retrieved";

	private static int PAGE_SIZE = 1000;
	private IDataService dataService;
	private ResponsibleDTO responsible;
	private int current = -1;
	private int currentPage = 0;

	private List<ResponsibleFilesDTO> data = new ArrayList<ResponsibleFilesDTO>();
	private HashMap<String, Object> filters;
	
	public JRResponsibleFilesDS(IDataService dataService, ResponsibleDTO responsible, boolean includeResponsible2, boolean includeClosed) throws DataServiceException {
		
		this.dataService = dataService;
		this.responsible = responsible;
		
        // Siempre se filtra por responsable
        filters = new HashMap<String, Object>();
        
        if (responsible != null)
        	filters.put("responsable1", responsible);
        
        if (includeResponsible2) {
        	filters.remove("responsable1");
        	filters.put("responsable2", responsible);
        }
        							        
		
        if (!includeClosed) {
			filters.put("cerrado", new Boolean(false));
		}
        
		load();
	}

	@Override
	public Object getFieldValue(JRField field) throws JRException {
		ResponsibleFilesDTO dto = data.get(current);
		String propertyName = field.getName();
		try {			
			return  PropertyUtils.getProperty(dto, propertyName);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw 
			new JRException(
				EXCEPTION_MESSAGE_KEY_BEAN_FIELD_VALUE_NOT_RETRIEVED,
				new Object[]{propertyName}, e);
		}
	}

	@Override
	public boolean next() throws JRException {
		
		boolean result = true;
		
		// Primero se ve si hay que cargar más datos
		current++;
		if (current >= data.size()) {
			currentPage++;
			current = 0;
			try {
				load();
			} catch (DataServiceException e) {
				throw new JRException(e);
			}
		}
		
		// Se ha llegado al final
		if (current >= data.size()) {
			result = false;
		}
		
		return result;
	}

	private void load() throws DataServiceException {
		data = dataService.getAllResponsibleFiles(PAGE_SIZE, ((PAGE_SIZE)*currentPage), filters, "apellidos", false);
	}
}

package es.salazaryasociados.gestorui.view.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import es.salazaryasociados.services.data.dto.ResponsibleDTO;

/**
 * The Class SelectOneMenuConverter.
 */
@FacesConverter("responsibleConverter")
public class ResponsibleConverter implements Converter {
 
    @Override
    public ResponsibleDTO getAsObject(final FacesContext context, final UIComponent ui, final String objectString) {
        if (objectString == null) {
            return null;
        }
 
        ResponsibleDTO obj = null;
     // deserialize the object
        try {
        	
        	String[] values = objectString.split("#");
        	obj = new ResponsibleDTO();
        	obj.setId(Integer.decode(values[0]));
        	obj.setNombre(values[1]);
        	obj.setEmail(values[2]);
        } catch (Exception e) {
            return null;
        }
        return obj;
    }
 
   @Override
    public String getAsString(final FacesContext arg0, final UIComponent arg1, final Object object) {
	   
	   String result = null;
	   if (object instanceof ResponsibleDTO) {
		   ResponsibleDTO dto = (ResponsibleDTO)object;
		   if (object != null) {
			   result = dto.getId() + "#" + dto.getNombre() + "#" + dto.getEmail();
		   }
	   }
	   return result;
    }
 
}
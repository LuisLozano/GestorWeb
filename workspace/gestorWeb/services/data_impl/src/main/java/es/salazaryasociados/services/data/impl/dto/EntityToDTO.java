package es.salazaryasociados.services.data.impl.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EntityToDTO<Entity, DTO> {
	
	static Logger logger = LoggerFactory.getLogger(EntityToDTO.class);
	
	private Class<Entity> entityClass;
	private Class<DTO> dtoClass;

	public EntityToDTO(Class<Entity> entityClass, Class<DTO> dtoClass) {
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}
	
	public Entity getEntity(DTO dto) {
		
		Entity result = createEntity();		
		final Field[] fields =  
				entityClass.getDeclaredFields();
		
		if (fields != null) {
			for (Field field : fields) {
				Object value;
				try {
					String propertyName = field.getName();
					value = getPropertyValue(dto, propertyName);					
					setPropertyValue(result, propertyName, value);
				} catch (NoSuchMethodException e) {
					logger.debug(e.getMessage());
				} catch (IllegalAccessException e) {
					logger.debug(e.getMessage());
				} catch (InvocationTargetException e) {
					logger.debug(e.getMessage());
				}				
			}
		}
		return result;
	}
	
	public DTO getDto(Entity entity) {
		
		DTO result = createDTO();		
		final Field[] fields =  
				dtoClass.getDeclaredFields();
		
		if (fields != null) {
			for (Field field : fields) {
				Object value;
				try {
					String propertyName = field.getName();
					value = getPropertyValue(entity, propertyName);					
					setPropertyValue(result, propertyName, value);
				} catch (NoSuchMethodException e) {
					logger.debug(e.getMessage());
				} catch (IllegalAccessException e) {
					logger.debug(e.getMessage());
				} catch (InvocationTargetException e) {
					logger.debug(e.getMessage());
				}				
			}
		}
		return result;
	}	
	
	public List<DTO> getDtoList(List<Entity> entityList) {
		List<DTO> result = new ArrayList<DTO>();
		
		if (entityList != null && entityList.size() > 0) {
			for (Entity entity : entityList) {
				DTO dto = getDto(entity);
				result.add(dto);
			}
		}
		
		return result;
	}
	
	private void setPropertyValue(Object result, String propertyName, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PropertyUtils.setSimpleProperty(result, propertyName, value);
	}

	private Object getPropertyValue(Object entity, String propertyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return PropertyUtils.getSimpleProperty(entity, propertyName);
	}

	public abstract Entity createEntity();
	public abstract DTO createDTO();	
}

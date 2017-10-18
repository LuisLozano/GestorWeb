package es.salazaryasociados.gestorui;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SalazarBean implements Serializable {

	private static final long serialVersionUID = 1520043376879151884L;
	protected Logger logger = LoggerFactory.getLogger(SalazarBean.class);

	public void init() {
		this.initInjection(this.getClass());		
	}
	
	protected void initInjection(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			for (Field field : fields) {
				if (field != null && field.isAnnotationPresent(javax.inject.Inject.class)) {
					Object service = ServicesProvider.GetService(field.getType());
					try {
						field.setAccessible(true);
						field.set(this, service);
					} catch (IllegalArgumentException e) {
						throw e;
					} catch (IllegalAccessException e) {
						throw new IllegalArgumentException("No ha definido método set para " + field.getName(), e);
					}
				}
			}
		}
	}
	
}

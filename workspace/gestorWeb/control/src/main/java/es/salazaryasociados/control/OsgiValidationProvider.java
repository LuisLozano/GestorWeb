package es.salazaryasociados.control;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationProviderResolver;
import javax.validation.spi.ValidationProvider;

import org.hibernate.validator.HibernateValidator;

public class OsgiValidationProvider implements ValidationProviderResolver {

	@Override
	public List<ValidationProvider<?>> getValidationProviders() {
		 List<ValidationProvider<?>> providers = new ArrayList<ValidationProvider<?>>(1);
	        providers.add(new HibernateValidator());
	        return providers;
	}

}

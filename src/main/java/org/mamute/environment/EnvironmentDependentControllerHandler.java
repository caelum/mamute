package org.mamute.environment;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import br.com.caelum.vraptor.controller.BeanClass;
import br.com.caelum.vraptor.core.ControllerQualifier;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.http.route.RoutesParser;
import br.com.caelum.vraptor.ioc.ControllerHandler;

@Specializes
public class EnvironmentDependentControllerHandler extends ControllerHandler{

	private Environment environment;

	@Inject
	public EnvironmentDependentControllerHandler(Router router, RoutesParser parser, ServletContext context, Environment environment) {
		super(router, parser, context);
		this.environment = environment;
	}

	/**
	 * @deprecated CDI eyes only
	 */
	protected EnvironmentDependentControllerHandler() {
	}
	
	
	@Override
	public void handle(@Observes @ControllerQualifier BeanClass controller) {
		Class<?> clazz = controller.getType();
		if(!clazz.isAnnotationPresent(EnvironmentDependent.class)) {
			super.handle(controller);
			return;
		}

		EnvironmentDependent annotation = clazz.getAnnotation(EnvironmentDependent.class);
		String feature = annotation.supports();
		if(environment.supports(feature)) 
			super.handle(controller);
		
		
	}
	
}

package br.com.caelum.brutal.providers;

import br.com.caelum.vraptor.ComponentRegistry;
import br.com.caelum.vraptor.ioc.spring.SpringProvider;

public class ContextProvider extends SpringProvider {
	
	@Override
	protected void registerCustomComponents(ComponentRegistry registry) {
		registry.register(SessionFactoryCreator.class,
				SessionFactoryCreator.class);
	    registry.register(SessionProvider.class, 
	    		SessionProvider.class);
    }

}

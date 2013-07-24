package br.com.caelum.brutal.brutauth.auth.handlers;

import br.com.caelum.brutal.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.Container;

@Component
public class HandlerSearcher {
	private final Container container;

	public HandlerSearcher(Container container) {
		this.container = container;
	}
	
	public RuleHandler getHandler(BrutauthRule rule) {
		if(containsSpecificHandler(rule)){
			HandledBy handledBy = rule.getClass().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());		
		}else{
			return container.instanceFor(AccessNotPermitedHandler.class);
		}
	}
	
	private boolean containsSpecificHandler(BrutauthRule rule) {
		return rule.getClass().isAnnotationPresent(HandledBy.class);
	}

}

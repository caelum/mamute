package br.com.caelum.brutal.brutauth.interceptors;

import java.util.Arrays;

import br.com.caelum.brutal.brutauth.AccessLevel;
import br.com.caelum.brutal.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutal.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutal.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class SimpleBrutauthRuleInterceptor implements Interceptor {
	
	private Container container;
	private final HandlerSearcher handlers;

	public SimpleBrutauthRuleInterceptor(Container container, HandlerSearcher handlers) {
		this.container = container;
		this.handlers = handlers;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		
		SimpleBrutauthRules permissionAnnotation = method.getMethod().getAnnotation(SimpleBrutauthRules.class);
		Class<? extends SimpleBrutauthRule>[] permissions = permissionAnnotation.value();
		long permissionData = 0l;
		Arrays.asList(method.getMethod().getAnnotations());
		if (method.containsAnnotation(AccessLevel.class)) {
			permissionData = method.getMethod().getAnnotation(AccessLevel.class).value();
		}
		for (Class<? extends SimpleBrutauthRule> permission : permissions) {
			SimpleBrutauthRule rule = container.instanceFor(permission);
			RuleHandler handler = handlers.getHandler(rule);
			if (!handler.handle(rule.isAllowed(permissionData))) {
				return;
			}
		}
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(SimpleBrutauthRules.class);
	}

}

package br.com.caelum.brutal.brutauth.interceptors;

import java.util.Arrays;

import br.com.caelum.brutal.brutauth.AccessLevel;
<<<<<<< HEAD
import br.com.caelum.brutal.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutal.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutal.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
=======
import br.com.caelum.brutal.brutauth.SimpleBrutauthRule;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;
<<<<<<< HEAD
=======
import br.com.caelum.vraptor.view.Results;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159

@Intercepts
public class SimpleBrutauthRuleInterceptor implements Interceptor {
	
	private Container container;
<<<<<<< HEAD
	private final HandlerSearcher handlers;

	public SimpleBrutauthRuleInterceptor(Container container, HandlerSearcher handlers) {
		this.container = container;
		this.handlers = handlers;
=======
	private Result result;

	public SimpleBrutauthRuleInterceptor(Container container, Result result) {
		this.container = container;
		this.result = result;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		
<<<<<<< HEAD
		SimpleBrutauthRules permissionAnnotation = method.getMethod().getAnnotation(SimpleBrutauthRules.class);
		Class<? extends SimpleBrutauthRule>[] permissions = permissionAnnotation.value();
=======
		SimpleBrutauthRule permissionAnnotation = method.getMethod().getAnnotation(SimpleBrutauthRule.class);
		Class<? extends BrutauthRule>[] permissions = permissionAnnotation.value();
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
		long permissionData = 0l;
		Arrays.asList(method.getMethod().getAnnotations());
		if (method.containsAnnotation(AccessLevel.class)) {
			permissionData = method.getMethod().getAnnotation(AccessLevel.class).value();
		}
<<<<<<< HEAD
		for (Class<? extends SimpleBrutauthRule> permission : permissions) {
			SimpleBrutauthRule rule = container.instanceFor(permission);
			RuleHandler handler = handlers.getHandler(rule);
			if (!handler.handle(rule.isAllowed(permissionData))) {
=======
		for (Class<? extends BrutauthRule> permission : permissions) {
			BrutauthRule rule = container.instanceFor(permission);
			if (!rule.isAllowed(permissionData)) {
				result.use(Results.http()).sendError(403);
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
				return;
			}
		}
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
<<<<<<< HEAD
		return method.containsAnnotation(SimpleBrutauthRules.class);
=======
		return method.containsAnnotation(SimpleBrutauthRule.class);
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
	}

}

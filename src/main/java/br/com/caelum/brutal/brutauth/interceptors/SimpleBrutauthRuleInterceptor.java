package br.com.caelum.brutal.brutauth.interceptors;

import java.util.Arrays;

import br.com.caelum.brutal.brutauth.AccessLevel;
import br.com.caelum.brutal.brutauth.SimpleBrutauthRule;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;

@Intercepts
public class SimpleBrutauthRuleInterceptor implements Interceptor {
	
	private Container container;
	private Result result;

	public SimpleBrutauthRuleInterceptor(Container container, Result result) {
		this.container = container;
		this.result = result;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		
		SimpleBrutauthRule permissionAnnotation = method.getMethod().getAnnotation(SimpleBrutauthRule.class);
		Class<? extends BrutauthRule>[] permissions = permissionAnnotation.value();
		long permissionData = 0l;
		Arrays.asList(method.getMethod().getAnnotations());
		if (method.containsAnnotation(AccessLevel.class)) {
			permissionData = method.getMethod().getAnnotation(AccessLevel.class).value();
		}
		for (Class<? extends BrutauthRule> permission : permissions) {
			BrutauthRule rule = container.instanceFor(permission);
			if (!rule.isAllowed(permissionData)) {
				result.use(Results.http()).sendError(403);
				return;
			}
		}
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(SimpleBrutauthRule.class);
	}

}

package br.com.caelum.brutal.auth.rules;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.auth.LoggedUserInterceptor;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(after = LoggedUserInterceptor.class)
public class MinimumReputationInterceptor implements Interceptor {

	private final LoggedUser loggedUser;
	private final Result result;

	public MinimumReputationInterceptor(LoggedUser loggedUser, Result result) {
		this.loggedUser = loggedUser;
		this.result = result;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(MinimumReputation.class);
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {

		MinimumReputation annotation = method.getMethod().getAnnotation(
				MinimumReputation.class);
		int minimum = annotation.value();
		MinimumKarmaRule<Void> minimumKarmaRule = new MinimumKarmaRule<>(
				minimum);
		if (!minimumKarmaRule.isAllowed(loggedUser.getCurrent(), null)) {
			result.use(http()).sendError(403);	
			return;
		}
		stack.next(method, resourceInstance);
	}

}

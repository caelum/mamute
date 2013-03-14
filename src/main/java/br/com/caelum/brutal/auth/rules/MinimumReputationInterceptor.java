package br.com.caelum.brutal.auth.rules;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.auth.LoggedUserInterceptor;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(after = LoggedUserInterceptor.class)
public class MinimumReputationInterceptor implements Interceptor {

	private final LoggedUser loggedUser;
	private final Result result;
	private final Localization localization;

	public MinimumReputationInterceptor(LoggedUser loggedUser, Result result, Localization localization) {
		this.loggedUser = loggedUser;
		this.result = result;
		this.localization = localization;
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
		MinimumKarmaRule<Void> hasEnoughKarma = new MinimumKarmaRule<>(minimum);
		ModeratorRule<Void> isModerador = new ModeratorRule<>();
		ComposedRule<Void> rule = new ComposedRule<>();
		
		rule = rule.thiz(hasEnoughKarma).or(isModerador);
		
		if (!rule.isAllowed(loggedUser.getCurrent(), null)) {
			result.use(http()).body(localization.getMessage("error.not_allowed")).setStatusCode(403);
			return;
		}
		stack.next(method, resourceInstance);
	}

}

package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.auth.LoggedUserInterceptor;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(after=LoggedUserInterceptor.class)
public class ReputationEventInterceptor implements Interceptor {
	
	private final User currentUser;
	private final KarmaCalculator karmaCalculator;

	public ReputationEventInterceptor(KarmaCalculator karmaCalculator, User currentUser) {
		this.karmaCalculator = karmaCalculator;
		this.currentUser = currentUser;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(ReputationEvent.class);
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		ReputationEvent annotation = method.getMethod().getAnnotation(ReputationEvent.class);
		ReputationEvents event = annotation.value();
		int karmaAwarded = karmaCalculator.karmaFor(event);
		currentUser.increaseKarma(karmaAwarded);
		stack.next(method, resourceInstance);
	}


}

package org.mamute.brutauth.auth.rules;

import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.MethodExecuted;
import org.mamute.controllers.EnvironmentAccessLevel;
import org.mamute.model.LoggedUser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import java.lang.reflect.Method;

import static org.mamute.auth.rules.ComposedRule.composedRule;
import static org.mamute.auth.rules.Rules.hasKarma;
import static org.mamute.auth.rules.Rules.isModerator;

@RequestScoped
public class EnvironmentKarmaRule implements SimpleBrutauthRule {

	@Inject
	private EnvironmentKarma environmentKarma;
	@Inject
	private ControllerMethod method;
	@Inject
	private LoggedUser user;

	@Override
	public boolean isAllowed(long l) {
		if (!method.containsAnnotation(EnvironmentAccessLevel.class)) {
			throw new IllegalStateException("To use EnvironmentKarmaRule you must annotate the method with EnvironmentAccessLevel");
		}
		if (!user.isLoggedIn()) {
			return false;
		}
		Method rawMethod = method.getMethod();
		EnvironmentAccessLevel annotation = rawMethod.getAnnotation(EnvironmentAccessLevel.class);
		long karma = environmentKarma.get(annotation.value());
		return composedRule(isModerator())
				.or(hasKarma(karma))
				.isAllowed(user.getCurrent(), null);
	}

}

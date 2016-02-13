package org.mamute.brutauth.auth.rules;

import javax.inject.Inject;

import org.mamute.brutauth.auth.handlers.LoggedHandler;
import org.mamute.model.LoggedUser;

import br.com.caelum.brutauth.auth.annotations.GlobalRule;
import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.environment.Environment;

@GlobalRule
@HandledBy(LoggedHandler.class)
public class GlobalShouldBeLoggedRule implements CustomBrutauthRule {

	@Inject private LoggedUser loggedUser;
	@Inject private Environment env;

	public boolean isAllowed() {
		if(!env.supports("feature.login.required")) return true;
		return loggedUser.isLoggedIn();
	}

}

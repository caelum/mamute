package br.com.caelum.brutal.brutauth.auth.rules;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.handlers.LoggedHandler;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@HandledBy(LoggedHandler.class)
public class LoggedRule implements CustomBrutauthRule {
	
	@Inject private LoggedUser loggedUser;

	public boolean isAllowed() {
		return loggedUser.isLoggedIn();
	}

}

package br.com.caelum.brutal.brutauth.rules;

import br.com.caelum.brutal.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutal.brutauth.auth.handlers.LoggedHandler;
import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
@HandledBy(LoggedHandler.class)
public class LoggedRule implements CustomBrutauthRule {
	
	private LoggedUser loggedUser;

	public LoggedRule(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	public boolean isAllowed() {
		return loggedUser.isLoggedIn();
	}

}

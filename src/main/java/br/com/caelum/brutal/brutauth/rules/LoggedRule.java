package br.com.caelum.brutal.brutauth.rules;

import br.com.caelum.brutal.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutal.brutauth.auth.handlers.ToLoginHandler;
import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
@HandledBy(ToLoginHandler.class)
public class LoggedRule implements SimpleBrutauthRule {
	
	private LoggedUser loggedUser;

	public LoggedRule(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public boolean isAllowed(long permissionData) {
		return loggedUser.isLoggedIn();
	}

}

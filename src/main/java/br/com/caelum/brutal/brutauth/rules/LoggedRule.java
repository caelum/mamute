package br.com.caelum.brutal.brutauth.rules;

import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class LoggedRule implements BrutauthRule {
	
	private LoggedUser loggedUser;

	public LoggedRule(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public boolean isAllowed(long permissionData) {
		return loggedUser.isLoggedIn();
	}

}

package br.com.caelum.brutal.brutauth.auth.rules;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthModeratorRule implements CustomBrutauthRule {

	private final LoggedUser loggedUser;

	public BrutauthModeratorRule(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public boolean isAllowed(Question question, String sluggedTitle) {
		return loggedUser.isModerator();
	}

}

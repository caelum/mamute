package br.com.caelum.brutal.brutauth.auth.rules;

import br.com.caelum.brutal.model.LoggedUser;
<<<<<<< HEAD
=======
import br.com.caelum.brutal.model.Question;
>>>>>>> criando anotação e interceptor para regras mais simples
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthModeratorRule implements CustomBrutauthRule {

	private final LoggedUser loggedUser;

	public BrutauthModeratorRule(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
<<<<<<< HEAD
	public boolean isAllowed() {
=======
	public boolean isAllowed(Question question, String sluggedTitle) {
>>>>>>> criando anotação e interceptor para regras mais simples
		return loggedUser.isModerator();
	}

}

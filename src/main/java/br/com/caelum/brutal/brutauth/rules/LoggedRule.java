package br.com.caelum.brutal.brutauth.rules;

<<<<<<< HEAD
import br.com.caelum.brutal.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutal.brutauth.auth.handlers.ToLoginHandler;
import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;
=======
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
<<<<<<< HEAD
@HandledBy(ToLoginHandler.class)
public class LoggedRule implements SimpleBrutauthRule {
=======
public class LoggedRule implements BrutauthRule {
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
	
	private LoggedUser loggedUser;

	public LoggedRule(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public boolean isAllowed(long permissionData) {
		return loggedUser.isLoggedIn();
	}

}

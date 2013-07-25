package br.com.caelum.brutal.brutauth.auth.rules;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ModeratorOnlyRule implements CustomBrutauthRule{
	
	private final LoggedUser user;

	public ModeratorOnlyRule(LoggedUser user) {
		this.user = user;
	}
	
	public boolean isAllowed(){
		return user.isModerator();
	}
	
}

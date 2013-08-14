package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.annotation.Nullable;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@Component
public class ModeratorOnlyRule implements CustomBrutauthRule{
	
	private final User user;

	public ModeratorOnlyRule(@Nullable User user) {
		this.user = user;
	}
	
	public boolean isAllowed(){
		return isModerator().isAllowed(user, null);
	}
	
}

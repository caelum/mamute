package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class ModeratorOnlyRule implements CustomBrutauthRule{
	
	@Inject private User user;

	
	public boolean isAllowed(){
		return isModerator().isAllowed(user, null);
	}
	
}

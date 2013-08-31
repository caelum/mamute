package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import static br.com.caelum.brutal.auth.rules.Rules.hasKarma;
import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;

public class ModeratorOrKarmaRule implements SimpleBrutauthRule {
	
	@Inject private LoggedUser user;


	@Override
	public boolean isAllowed(long accessLevel) {
		if(!user.isLoggedIn()) return false;
		long karma = accessLevel;
		return composedRule(isModerator()).or(hasKarma(karma)).isAllowed(user.getCurrent(), null); 
	}

}

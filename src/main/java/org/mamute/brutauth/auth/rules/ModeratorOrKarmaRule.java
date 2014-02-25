package org.mamute.brutauth.auth.rules;

import static org.mamute.auth.rules.ComposedRule.composedRule;
import static org.mamute.auth.rules.Rules.hasKarma;
import static org.mamute.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.mamute.model.LoggedUser;

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

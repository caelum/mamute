package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import static br.com.caelum.brutal.auth.rules.Rules.hasKarma;
import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.annotation.Nullable;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;

@Component
public class ModeratorOrKarmaRule implements SimpleBrutauthRule {
	
	private User user;

	public ModeratorOrKarmaRule(@Nullable User user) {
		this.user = user;
	}

	@Override
	public boolean isAllowed(long accessLevel) {
		long karma = accessLevel;
    	return composedRule(isModerator()).or(hasKarma(karma)).isAllowed(user, null); 
	}

}

package br.com.caelum.brutal.brutauth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;

import javax.annotation.Nullable;

import br.com.caelum.brutal.auth.rules.MinimumKarmaRule;
import br.com.caelum.brutal.auth.rules.ModeratorRule;
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ModeratorOrKarmaRule implements BrutauthRule {
	
	private User user;

	public ModeratorOrKarmaRule(@Nullable User user) {
		this.user = user;
	}

	@Override
	public boolean isAllowed(long accessLevel) {
		long karma = accessLevel;
		
		MinimumKarmaRule<Void> hasEnoughKarma = new MinimumKarmaRule<>(karma);
    	ModeratorRule<Void> isModerator = new ModeratorRule<>();

    	return composedRule(isModerator).or(hasEnoughKarma).isAllowed(user, null); 
	}

}

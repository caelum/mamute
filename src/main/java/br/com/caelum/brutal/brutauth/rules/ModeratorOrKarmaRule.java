package br.com.caelum.brutal.brutauth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;

import javax.annotation.Nullable;

import br.com.caelum.brutal.auth.rules.MinimumKarmaRule;
import br.com.caelum.brutal.auth.rules.ModeratorRule;
<<<<<<< HEAD
import br.com.caelum.brutal.brutauth.auth.rules.SimpleBrutauthRule;
=======
import br.com.caelum.brutal.brutauth.auth.rules.BrutauthRule;
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
<<<<<<< HEAD
public class ModeratorOrKarmaRule implements SimpleBrutauthRule {
=======
public class ModeratorOrKarmaRule implements BrutauthRule {
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
	
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

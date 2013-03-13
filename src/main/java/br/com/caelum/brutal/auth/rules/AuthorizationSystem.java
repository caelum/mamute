package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AuthorizationSystem {

	private User user;

	public AuthorizationSystem(User user) {
		this.user = user;
	}

	public void authorize(Moderatable question, int karmaRequired) {
		AuthorRule<Moderatable> authorRule = new AuthorRule<Moderatable>();
		MinimumKarmaRule<Moderatable> minimumKarmaRule = new MinimumKarmaRule<>(karmaRequired);
		
		boolean dontHaveEnoughKarma = !minimumKarmaRule.isAllowed(user, question);
		boolean isNoTheAuthor = !authorRule.isAllowed(user, question);
		if (isNoTheAuthor && dontHaveEnoughKarma)
			throw new UnauthorizedException("you are not the author or don't have enough karma"); // i18n here?
	}

}

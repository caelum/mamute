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
	
	/**
	 * @throws UnauthorizedException if the user isn't allowed to edit this moderatable
	 */
	public boolean canEdit(Moderatable question, int karmaRequired) {
		AuthorRule<Moderatable> isAuthor = new AuthorRule<Moderatable>();
		PermissionRule<Moderatable> hasEnoughKarma = new MinimumKarmaRule<>(karmaRequired);
		ComposedRule<Moderatable> composed = new ComposedRule<>();
		
		ComposedRule<Moderatable> rule = composed.thiz(isAuthor).or(hasEnoughKarma);
		
		if (rule.isAllowed(user, question))
			return true;
		throw new UnauthorizedException("you are not the author or don't have enough karma"); // i18n here?
	}

}

package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.interfaces.Moderatable;

public class Rules{
	public static ModeratorRule<Moderatable> isModerator() {
		ModeratorRule<Moderatable> moderatorRule = new ModeratorRule<>();
		return moderatorRule;
	}

	public static PermissionRule<Moderatable> hasKarma(int karmaRequired) {
		PermissionRule<Moderatable> hasEnoughKarma = new MinimumKarmaRule<>(karmaRequired);
		return hasEnoughKarma;
	}

	public static AuthorRule<Moderatable> isAuthor() {
		AuthorRule<Moderatable> isAuthor = new AuthorRule<Moderatable>();
		return isAuthor;
	}
}

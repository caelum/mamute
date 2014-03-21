package org.mamute.auth.rules;

import org.mamute.model.interfaces.Moderatable;

public class Rules{
	public static ModeratorRule<Moderatable> isModerator() {
		ModeratorRule<Moderatable> moderatorRule = new ModeratorRule<>();
		return moderatorRule;
	}

	public static PermissionRule<Moderatable> hasKarma(long karmaRequired) {
		PermissionRule<Moderatable> hasEnoughKarma = new MinimumKarmaRule<>(karmaRequired);
		return hasEnoughKarma;
	}

	public static AuthorRule<Moderatable> isAuthor() {
		AuthorRule<Moderatable> isAuthor = new AuthorRule<Moderatable>();
		return isAuthor;
	}
}

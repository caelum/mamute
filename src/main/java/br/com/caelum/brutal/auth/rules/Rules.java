package br.com.caelum.brutal.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import br.com.caelum.brutal.model.interfaces.Moderatable;

public enum Rules {
	
	EDIT_QUESTION {
		@Override
		public PermissionRule<Moderatable> build() {
			ComposedRule<Moderatable> rule = editPostRule(PermissionRulesConstants.EDIT_QUESTION);
			return rule;
		}
	},
	EDIT_ANSWER {
		@Override
		public PermissionRule<Moderatable> build() {
			ComposedRule<Moderatable> rule = editPostRule(PermissionRulesConstants.EDIT_ANSWER);
			return rule;
		}
	};
	
	public abstract PermissionRule<Moderatable> build();
	
	private static ComposedRule<Moderatable> editPostRule(int karmaRequired) {
		return composedRule(isAuthor()).or(hasKarma(karmaRequired)).or(isModerator());
	}

	private static ModeratorRule<Moderatable> isModerator() {
		ModeratorRule<Moderatable> moderatorRule = new ModeratorRule<>();
		return moderatorRule;
	}

	private static PermissionRule<Moderatable> hasKarma(int karmaRequired) {
		PermissionRule<Moderatable> hasEnoughKarma = new MinimumKarmaRule<>(karmaRequired);
		return hasEnoughKarma;
	}

	private static AuthorRule<Moderatable> isAuthor() {
		AuthorRule<Moderatable> isAuthor = new AuthorRule<Moderatable>();
		return isAuthor;
	}
	
}

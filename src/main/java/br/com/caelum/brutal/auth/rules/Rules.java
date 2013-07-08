package br.com.caelum.brutal.auth.rules;

import br.com.caelum.brutal.model.interfaces.Moderatable;

public enum Rules {
	
	EDIT_QUESTION {
		@Override
		public PermissionRule<Moderatable> build() {
			ComposedRule<Moderatable> rule = composeModeratableRule(PermissionRulesConstants.EDIT_QUESTION);
			return rule;
		}
	},
	EDIT_ANSWER {
		@Override
		public PermissionRule<Moderatable> build() {
			ComposedRule<Moderatable> rule = composeModeratableRule(PermissionRulesConstants.EDIT_QUESTION);
			return rule;
		}
	};
	
	private static ComposedRule<Moderatable> composeModeratableRule(int karmaRequired) {
		AuthorRule<Moderatable> isAuthor = new AuthorRule<Moderatable>();
		PermissionRule<Moderatable> hasEnoughKarma = new MinimumKarmaRule<>(karmaRequired);
		ModeratorRule<Moderatable> moderatorRule = new ModeratorRule<>();
		ComposedRule<Moderatable> composed = new ComposedRule<>();
		ComposedRule<Moderatable> rule = composed.thiz(isAuthor).or(hasEnoughKarma).or(moderatorRule);
		return rule;
	}
	
	public abstract PermissionRule<Moderatable> build();
}

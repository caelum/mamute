package br.com.caelum.brutal.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;

public class Rules{
	
	
	
	public class EditNewsRule implements CustomBrutauthRule{
		private final User user;
		
		public EditNewsRule(User user) {
			this.user = user;
		}
		
		public boolean isAllowed(Question question) {
			return composedRule(isModerator()).or(isAuthor()).isAllowed(user, question);
		}
	}
	
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

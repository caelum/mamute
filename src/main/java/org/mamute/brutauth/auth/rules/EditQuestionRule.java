package org.mamute.brutauth.auth.rules;

import static org.mamute.auth.rules.ComposedRule.composedRule;
import static org.mamute.auth.rules.Rules.hasKarma;
import static org.mamute.auth.rules.Rules.isAuthor;
import static org.mamute.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.mamute.auth.rules.PermissionRules;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.User;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditQuestionRule implements CustomBrutauthRule{
	private User user;
	private EnvironmentKarma environmentKarma;

	@Deprecated
	public EditQuestionRule() {
	}

	@Inject
	public EditQuestionRule(LoggedUser user, EnvironmentKarma environmentKarma) {
		this.environmentKarma = environmentKarma;
		this.user = user.getCurrent();
	}

	public boolean isAllowed(Question question) {
		long karma = environmentKarma.get(PermissionRules.EDIT_QUESTION);
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, question);
	}
}
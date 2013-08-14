package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import static br.com.caelum.brutal.auth.rules.Rules.hasKarma;
import static br.com.caelum.brutal.auth.rules.Rules.isAuthor;
import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditQuestionRule implements CustomBrutauthRule{
	private User user;

	@Deprecated
	public EditQuestionRule() {
	}

	@Inject
	public EditQuestionRule(User user) {
		this.user = user;
	}

	public boolean isAllowed(Question question) {
		int karma = PermissionRulesConstants.EDIT_QUESTION;
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, question);
	}
}
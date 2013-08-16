package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import static br.com.caelum.brutal.auth.rules.Rules.hasKarma;
import static br.com.caelum.brutal.auth.rules.Rules.isAuthor;
import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditAnswerRule implements CustomBrutauthRule {
	private User user;

	@Deprecated
	public EditAnswerRule() {
	}
	
	@Inject
	public EditAnswerRule(LoggedUser user) {
		this.user = user.getCurrent();
	}

	public boolean isAllowed(Answer answer) {
		int karma = PermissionRulesConstants.EDIT_ANSWER;
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, answer);
	}
}


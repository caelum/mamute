package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import static br.com.caelum.brutal.auth.rules.Rules.hasKarma;
import static br.com.caelum.brutal.auth.rules.Rules.isAuthor;
import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.annotation.Nullable;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class EditQuestionRule implements CustomBrutauthRule{
	private final User user;
	
	public EditQuestionRule(@Nullable User user) {
		this.user = user;
	}
	
	public boolean isAllowed(Question question) {
		int karma = PermissionRulesConstants.EDIT_QUESTION;
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, question);
	}
}
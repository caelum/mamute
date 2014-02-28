package org.mamute.brutauth.auth.rules;

import static org.mamute.auth.rules.ComposedRule.composedRule;
import static org.mamute.auth.rules.Rules.isAuthor;
import static org.mamute.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.mamute.model.LoggedUser;
import org.mamute.model.News;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditNewsRule implements CustomBrutauthRule{
	@Inject private LoggedUser user;
	
	public boolean isAllowed(News news) {
		return composedRule(isModerator()).or(isAuthor()).isAllowed(user.getCurrent(), news);
	}
}

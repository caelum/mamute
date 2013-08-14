package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.ComposedRule.composedRule;
import static br.com.caelum.brutal.auth.rules.Rules.isAuthor;
import static br.com.caelum.brutal.auth.rules.Rules.isModerator;

import javax.annotation.Nullable;

import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@Component
public class EditNewsRule implements CustomBrutauthRule{
	private final User user;
	
	public EditNewsRule(@Nullable User user) {
		this.user = user;
	}
	
	public boolean isAllowed(News news) {
		return composedRule(isModerator()).or(isAuthor()).isAllowed(user, news);
	}
}

package br.com.caelum.brutal.brutauth.auth.rules;

import javax.annotation.Nullable;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthAuthorRule implements CustomBrutauthRule {

	private final User user;

	public BrutauthAuthorRule(@Nullable User user) {
		this.user = user;
	}
	
	public boolean isAllowed(Question question) {
		if (user == null) return false;
		if (question.getAuthor() == null) {
			throw new IllegalArgumentException("can't verify permissions on item without an author");
		}
		return question.getAuthor().getId().equals(user.getId());
	}


}

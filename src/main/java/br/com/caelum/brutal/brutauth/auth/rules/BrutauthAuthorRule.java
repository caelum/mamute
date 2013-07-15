package br.com.caelum.brutal.brutauth.auth.rules;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthAuthorRule implements BrutauthRule{

	private final User user;

	public BrutauthAuthorRule(User user) {
		this.user = user;
	}
	
	public boolean isAllowed(Question question, String sluggedTitle) {
		if (user == null) return false;
		if (question.getAuthor() == null) {
			throw new IllegalArgumentException("can't verify permissions on item without an author");
		}
		return question.getAuthor().getId().equals(user.getId());
	}


}

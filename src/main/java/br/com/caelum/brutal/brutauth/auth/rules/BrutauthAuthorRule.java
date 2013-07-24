package br.com.caelum.brutal.brutauth.auth.rules;

import javax.annotation.Nullable;

<<<<<<< HEAD
import br.com.caelum.brutal.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutal.brutauth.auth.handlers.AccessNotPermitedHandler;
=======
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
<<<<<<< HEAD
@HandledBy(AccessNotPermitedHandler.class)
=======
>>>>>>> 255f1e479d9e262cc07cbd1ffc7ecc0bdfc3c159
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

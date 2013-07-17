package br.com.caelum.brutal.brutauth.auth.rules;

<<<<<<< HEAD
import javax.annotation.Nullable;

=======
>>>>>>> criando anotação e interceptor para regras mais simples
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthAuthorRule implements CustomBrutauthRule {

	private final User user;

<<<<<<< HEAD
	public BrutauthAuthorRule(@Nullable User user) {
		this.user = user;
	}

	public boolean isAllowed(Question question) {
=======
	public BrutauthAuthorRule(User user) {
		this.user = user;
	}
	
	public boolean isAllowed(Question question, String sluggedTitle) {
>>>>>>> criando anotação e interceptor para regras mais simples
		if (user == null) return false;
		if (question.getAuthor() == null) {
			throw new IllegalArgumentException("can't verify permissions on item without an author");
		}
		return question.getAuthor().getId().equals(user.getId());
	}


}

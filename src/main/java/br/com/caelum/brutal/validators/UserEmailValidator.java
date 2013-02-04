package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
public class UserEmailValidator {
	private final Validator validator;

	public UserEmailValidator(Validator validator){
		this.validator = validator;
	}

	public boolean validate(User user) {
		if(user.getEmail() == null || user.getEmail().isEmpty()) validator.add(new I18nMessage("error", "user.email.empty"));
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

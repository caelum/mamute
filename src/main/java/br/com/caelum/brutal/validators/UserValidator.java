package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
public class UserValidator {

	private Validator validator;
	private EmailValidator emailValidator;

	public UserValidator(Validator validator, EmailValidator emailValidator) {
		this.validator = validator;
		this.emailValidator = emailValidator;
	}

	public boolean validate(User user) {
		if (user == null) {
			validator.add(new I18nMessage("error","user.errors.wrong"));
			return false;
		}
		
	    validator.validate(user);
	    
		emailValidator.validate(user.getEmail());
		
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

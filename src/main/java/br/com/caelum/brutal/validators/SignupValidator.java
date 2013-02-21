package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;


@Component
public class SignupValidator {
	private static final int MIN_LENGHT = 6;
	private final Validator validator;
	private UserValidator userValidator;

	public SignupValidator(Validator validator, UserValidator userValidator) {
		this.validator = validator;
		this.userValidator = userValidator;
	}
	
	public boolean validate(User user, String password, String passwordConfirmation){
		userValidator.validate(user);
		
		if (password.length() < MIN_LENGHT) {
			validator.add(new I18nMessage("error","signup.errors.password.length"));
		}
		if (!password.equals(passwordConfirmation)) {
		    validator.add(new I18nMessage("error","signup.errors.password_confirmation"));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
		
}

package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;


@Component
public class SignupValidator {
	private final Validator validator;
	private UserValidator userValidator;
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PASSWORD_MAX_LENGTH = 100;

	public SignupValidator(Validator validator, UserValidator userValidator) {
		this.validator = validator;
		this.userValidator = userValidator;
	}
	
	public boolean validate(User user, String password, String passwordConfirmation){
		
		userValidator.validate(user);
		
		if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH){
			validator.add(new ValidationMessage("user.errors.password.length", "error"));
		}
		
		if (!password.equals(passwordConfirmation)) {
		    validator.add(new ValidationMessage("signup.errors.password_confirmation", "error"));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
		
}

package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;


@Component
public class SignupValidator {
	private final Validator validator;
	private UserValidator userValidator;
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PASSWORD_MAX_LENGTH = 100;
	private MessageFactory messageFactory;

	public SignupValidator(Validator validator, UserValidator userValidator, MessageFactory messageFactory) {
		this.validator = validator;
		this.userValidator = userValidator;
		this.messageFactory = messageFactory;
	}
	
	public boolean validate(User user, String password, String passwordConfirmation){
		userValidator.validate(user);
		
		if (password == null || password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH){
			validator.add(messageFactory.build("error", "user.errors.password.length"));
		}
		
		if (password != null && !password.equals(passwordConfirmation)) {
		    validator.add(messageFactory.build("error", "signup.errors.password_confirmation"));
		}
		
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
		
}

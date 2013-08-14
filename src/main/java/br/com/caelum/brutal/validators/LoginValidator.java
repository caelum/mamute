package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.factory.MessageFactory;

@Component
public class LoginValidator {
	private final Validator validator;
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PASSWORD_MAX_LENGTH = 100;
	private MessageFactory messageFactory;
	private final EmailValidator emailValidator;

	public LoginValidator(Validator validator, MessageFactory messageFactory, EmailValidator emailValidator) {
		this.validator = validator;
		this.messageFactory = messageFactory;
		this.emailValidator = emailValidator;
	}
	
	public boolean validate(String email, String password){
		if (password == null || password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH){
			validator.add(messageFactory.build("error", "user.errors.password.length"));
		}
		if (email == null) {
			validator.add(messageFactory.build("error", "user.errors.email.required"));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}

}

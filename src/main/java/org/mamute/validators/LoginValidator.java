package org.mamute.validators;

import javax.inject.Inject;

import org.mamute.factory.MessageFactory;

import br.com.caelum.vraptor.validator.Validator;

public class LoginValidator {
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PASSWORD_MAX_LENGTH = 100;
	
	private Validator validator;
	private MessageFactory messageFactory;

	@Deprecated
	public LoginValidator() {
	}

	@Inject
	public LoginValidator(Validator validator, MessageFactory messageFactory) {
		this.validator = validator;
		this.messageFactory = messageFactory;
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

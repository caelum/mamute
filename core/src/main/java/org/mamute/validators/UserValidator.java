package org.mamute.validators;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.mamute.controllers.BrutalValidator;
import org.mamute.factory.MessageFactory;
import org.mamute.model.User;

import br.com.caelum.vraptor.validator.Validator;

@RequestScoped
public class UserValidator {

	private Validator validator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private BrutalValidator brutalValidator;

	@Deprecated
	public UserValidator() {
	}
	
	@Inject
	public UserValidator(Validator validator, EmailValidator emailValidator, MessageFactory messageFactory, BrutalValidator brutalValidator) {
		this.validator = validator;
		this.emailValidator = emailValidator;
		this.messageFactory = messageFactory;
		this.brutalValidator = brutalValidator;
	}

	public boolean validate(User user) {
		brutalValidator.validate(user);
		if (user == null) {
			validator.add(messageFactory.build("error", "user.errors.wrong"));
			return false;
		}

		emailValidator.validate(user.getEmail());
		
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

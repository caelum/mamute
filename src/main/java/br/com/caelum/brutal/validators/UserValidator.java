package br.com.caelum.brutal.validators;

import javax.inject.Inject;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.ioc.RequestScoped;

@RequestScoped
public class UserValidator {

	private Validator validator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;

	@Deprecated
	public UserValidator() {
	}
	
	@Inject
	public UserValidator(Validator validator, EmailValidator emailValidator, MessageFactory messageFactory) {
		this.validator = validator;
		this.emailValidator = emailValidator;
		this.messageFactory = messageFactory;
	}

	public boolean validate(User user) {
		if (user == null) {
			validator.add(messageFactory.build("error", "user.errors.wrong"));
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

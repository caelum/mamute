package org.mamute.validators;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.User;

import br.com.caelum.vraptor.validator.Validator;

@RequestScoped
public class SignupValidator {
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PASSWORD_MAX_LENGTH = 100;
	
	private UserValidator userValidator;
	private Validator validator;
	private MessageFactory messageFactory;
	private UserDAO users;
	
	@Deprecated
	public SignupValidator() {
	}

	@Inject
	public SignupValidator(Validator validator, UserValidator userValidator, MessageFactory messageFactory, UserDAO users) {
		this.validator = validator;
		this.userValidator = userValidator;
		this.messageFactory = messageFactory;
		this.users = users;
	}
	
	public boolean validate(User user, String password, String passwordConfirmation){
		if (user == null) return false;
		userValidator.validate(user);

		if(users.existsWithName(user.getName())){
			validator.add(messageFactory.build("error", "user.errors.name.used"));
		}
		
		if (password == null || password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH){
			validator.add(messageFactory.build("error", "signup.errors.password.length"));
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

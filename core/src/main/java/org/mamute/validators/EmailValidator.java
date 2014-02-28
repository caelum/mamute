package org.mamute.validators;

import javax.inject.Inject;

import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;

import br.com.caelum.vraptor.validator.Validator;

public class EmailValidator {
	
	private UserDAO users;
	private Validator validator;
	private MessageFactory messageFactory;
	
	@Deprecated
	public EmailValidator() {
	}

	@Inject
	public EmailValidator(Validator validator, UserDAO users, MessageFactory messageFactory) {
		this.users = users;
		this.validator = validator;
		this.messageFactory = messageFactory;
	}
	
	public boolean validate(String email) {
		if (users.existsWithEmail(email)) {
			validator.add(messageFactory.build("error", "user.errors.email.used"));
		}
		if (email == null) {
			validator.add(messageFactory.build("error", "user.errors.email.required"));
		}
		
		return !validator.hasErrors();
	}
}

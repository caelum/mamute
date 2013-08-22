package br.com.caelum.brutal.validators;

import javax.inject.Inject;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor4.Validator;

public class UserNameValidator {

	private Validator validator;
	private UserDAO users;
	private MessageFactory messageFactory;
	
	@Deprecated
	public UserNameValidator() {
	}
	
	@Inject
	public UserNameValidator(Validator validator, UserDAO users, MessageFactory messageFactory) {
		this.validator = validator;
		this.users = users;
		this.messageFactory = messageFactory;
	}
	
	public boolean validate(String name) {
		if(users.existsWithName(name)){
			validator.add(messageFactory.build("error", "user.errors.name.used"));
		}
		
		return !validator.hasErrors();
	}

}

package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class UserNameValidator {

	Validator validator;
	UserDAO users;
	private MessageFactory messageFactory;
	
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

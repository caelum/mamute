package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class UserNameValidator {

	Validator validator;
	UserDAO users;
	
	public UserNameValidator(Validator validator, UserDAO users) {
		this.validator = validator;
		this.users = users;
	}
	
	public boolean validate(String name) {
		if(users.existsWithName(name)){
			validator.add(new ValidationMessage("user.errors.name.used", "error"));
		}
		
		return !validator.hasErrors();
	}

}

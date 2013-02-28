package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class EmailValidator {
	
	private UserDAO users;
	private Validator validator;

	public EmailValidator(Validator validator, UserDAO users) {
		this.users = users;
		this.validator = validator;
	}
	
	public boolean validate(String email){
		if (users.existsWithEmail(email)) {
			validator.add(new ValidationMessage("user.errors.email.used", "error"));
		}
		
		return !validator.hasErrors();
	}
}

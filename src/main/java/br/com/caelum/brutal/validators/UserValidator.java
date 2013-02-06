package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;


@Component
public class UserValidator{
	private final Validator validator;
    private final UserDAO users;

	public UserValidator(Validator validator, UserDAO users) {
		this.validator = validator;
        this.users = users;
	}
	
	public boolean validate(User user, String password, String passwordConfirmation){
	    validator.validate(user);
		if (user == null) {
		    validator.add(new I18nMessage("error","user.errors.wrong"));
		    return false;
		}
		if (users.existsWithEmail(user.getEmail())) {
		    validator.add(new I18nMessage("error","user.errors.used_email"));
		}
		if (!password.equals(passwordConfirmation)) {
		    validator.add(new I18nMessage("error","user.errors.password_confirmation"));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
		
}

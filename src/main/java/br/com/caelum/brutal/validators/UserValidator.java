package br.com.caelum.brutal.validators;

import java.util.regex.Pattern;

import org.joda.time.LocalDate;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
public class UserValidator {

	private static final int MIN_LENGHT = 6;
	private Validator validator;
	private UserDAO users;

	public UserValidator(Validator validator, UserDAO users) {
		this.validator = validator;
		this.users = users;
	}

	public boolean validate(User user) {
	    validator.validate(user);
		if (user == null) {
		    validator.add(new I18nMessage("error","user.errors.wrong"));
		    return false;
		}
		
		emailValidation(user.getEmail());
		
		if (user.getName().length() < MIN_LENGHT) {
			validator.add(new I18nMessage("error","user.errors.name.length"));
		}
		
		return !validator.hasErrors();
	}

	
	public boolean validate(User user, String name, String email, LocalDate birthDate) {
		if (user == null) {
		    validator.add(new I18nMessage("error","user.errors.wrong"));
		}
		
		if (user.getName().length() < MIN_LENGHT) {
			validator.add(new I18nMessage("error","user.errors.name.length"));
		}
		
		if (birthDate == null) {
			validator.add(new I18nMessage("error", "user.errors.invalid_birth_date"));
		}
		
		if (!user.getEmail().equals(email)){
			emailValidation(email);
		}
		
		return !validator.hasErrors();
	}
	
	private void emailValidation(String email) {
		if (users.existsWithEmail(email)) {
			validator.add(new I18nMessage("error","user.errors.used_email"));
		}
		if (!isEmail(email)) {
			validator.add(new I18nMessage("error","user.errors.invalid_email"));
		}
	}
	
	private boolean isEmail(String email) {
		if(email == null) return false;
		Pattern containsDomain = Pattern.compile("(.*?)@(.*?)\\.com(\\.br)?");
		return containsDomain.matcher(email).find();
	}
	
	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

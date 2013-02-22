package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.dto.UserPersonalInfoDTO;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
public class UserPersonalInfoValidator {
	
	private static final int MIN_LENGHT = 6;
	private Validator validator;
	private EmailValidator emailValidator;

	public UserPersonalInfoValidator(Validator validator, EmailValidator emailValidator){
		this.validator = validator;
		this.emailValidator = emailValidator;
	}
	
	public boolean validate(UserPersonalInfoDTO info) {
		validator.validate(info);
		if (info.getUser() == null) {
		    validator.add(new I18nMessage("error","user.errors.wrong"));
		}
		
		if (info.getName().length() < MIN_LENGHT) {
			validator.add(new I18nMessage("error","user.errors.name.length"));
		}
		
		if (info.getBirthDate() == null) {
			validator.add(new I18nMessage("error", "user.errors.invalid_birth_date"));
		}
		
		if (!info.getUser().getEmail().equals(info.getEmail())){
			emailValidator.validate(info.getEmail());
		}
		
		return !validator.hasErrors();
	}
	
	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

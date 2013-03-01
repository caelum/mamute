package br.com.caelum.brutal.validators;

import org.joda.time.DateTime;

import br.com.caelum.brutal.dto.UserPersonalInfo;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class UserPersonalInfoValidator {
	
	public static final int NAME_MIN_LENGTH = 6;
	public static final int NAME_MAX_LENGTH = 100;
	
	public static final int WEBSITE_MIN_LENGTH = 0;
	public static final int WEBSITE_MAX_LENGHT = 200;
	
	public static final int EMAIL_MIN_LENGTH = 6;
	public static final int EMAIL_MAX_LENGTH = 100;
	
	public static final int ABOUT_MIN_LENGTH = 6;
	public static final int ABOUT_MAX_LENGTH = 500;
	
	public static final String NAME_LENGTH_MESSAGE = "user.errors.name.length";
	public static final String REALNAME_LENGTH_MESSAGE = "user.errors.realName.length";
	public static final String WEBSITE_LENGTH_MESSAGE = "user.errors.website.length";
	public static final String EMAIL_LENGTH_MESSAGE = "user.errors.email.length";
	public static final String ABOUT_LENGTH_MESSAGE = "user.errors.about.length";
	
	public static final String EMAIL_NOT_VALID = "user.errors.email.invalid";
	
	public static final String NAME_REQUIRED = "user.errors.name.required";
	
	private Validator validator;
	private EmailValidator emailValidator;
	private UserNameValidator userNameValidator;

	public UserPersonalInfoValidator(Validator validator, EmailValidator emailValidator, UserNameValidator userNameValidator){
		this.validator = validator;
		this.emailValidator = emailValidator;
		this.userNameValidator = userNameValidator;
	}
	
	public boolean validate(UserPersonalInfo info) {
		
		validator.validate(info);
		
		if (info.getUser() == null) {
		    validator.add(new ValidationMessage("user.errors.wrong", "error"));
		}
		
		if(!info.getUser().getName().equals(info.getName())){
			userNameValidator.validate(info.getName());
		}
		
		if(!info.getUser().getEmail().equals(info.getEmail())){
			emailValidator.validate(info.getEmail());
		}
		
		if(info.getUser().getNameLastTouchedAt() != null){
			if(info.getUser().getNameLastTouchedAt().isAfter(new DateTime().minusDays(30))){
				validator.add(new ValidationMessage("user.errors.name.min_time", "error"));
			}
		}
		
		return !validator.hasErrors();
	}
	
	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

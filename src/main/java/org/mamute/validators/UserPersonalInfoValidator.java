package org.mamute.validators;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.mamute.controllers.BrutalValidator;
import org.mamute.dto.UserPersonalInfo;
import org.mamute.factory.MessageFactory;

import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.validator.Validator;

public class UserPersonalInfoValidator {
	
	public static final int NAME_MIN_LENGTH = 4;
	public static final int NAME_MAX_LENGTH = 100;
	public static final int WEBSITE_MIN_LENGTH = 0;
	public static final int WEBSITE_MAX_LENGHT = 200;
	public static final int EMAIL_MIN_LENGTH = 6;
	public static final int EMAIL_MAX_LENGTH = 100;
	public static final int LOCATION_MAX_LENGTH = 100;
	public static final int ABOUT_MIN_LENGTH = 6;
	public static final int ABOUT_MAX_LENGTH = 500;
	public static final int MARKED_ABOUT_MAX_LENGTH = 600;
	public static final String NAME_LENGTH_MESSAGE = "user.errors.name.length";
	public static final String LOCATION_LENGTH_MESSAGE = "user.errors.location.length";
	public static final String WEBSITE_LENGTH_MESSAGE = "user.errors.website.length";
	public static final String EMAIL_LENGTH_MESSAGE = "user.errors.email.length";
	public static final String ABOUT_LENGTH_MESSAGE = "user.errors.about.length";
	public static final String EMAIL_NOT_VALID = "user.errors.email.invalid";
	public static final String NAME_REQUIRED = "user.errors.name.required";
	
	private Validator validator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private BundleFormatter bundle;
	private BrutalValidator brutalValidator;

	@Deprecated
	public UserPersonalInfoValidator() {
	}

	@Inject
	public UserPersonalInfoValidator(Validator validator, EmailValidator emailValidator, 
			MessageFactory messageFactory, BundleFormatter bundle, BrutalValidator brutalValidator){
		this.validator = validator;
		this.emailValidator = emailValidator;
		this.messageFactory = messageFactory;
		this.bundle = bundle;
		this.brutalValidator = brutalValidator;
	}
	
	public boolean validate(UserPersonalInfo info) {
		brutalValidator.validate(info);
		if(validator.hasErrors()){
			return false;
		}
		
		if (info.getUser() == null) {
		    validator.add(messageFactory.build("error", "user.errors.wrong"));
		    return false;
		}
		
		if (!info.getUser().getEmail().equals(info.getEmail())){
			emailValidator.validate(info.getEmail());
		}
		
		if (info.getBirthDate() != null && info.getBirthDate().getYear() > DateTime.now().getYear()-12){
			validator.add(messageFactory.build("error", "user.errors.invalid_birth_date.min_age"));
		}
	
		if(!info.getUser().getName().equals(info.getName())){
			DateTime nameLastTouchedAt = info.getUser().getNameLastTouchedAt();
			if (nameLastTouchedAt != null && nameLastTouchedAt.isAfter(new DateTime().minusDays(30))) {
				validator.add(messageFactory.build(
						"error", 
						"user.errors.name.min_time", 
						nameLastTouchedAt.plusDays(30).toString(DateTimeFormat.forPattern(bundle.getMessage("date.joda.simple.pattern")))
				));
			}
		}
		
		return !validator.hasErrors();
	}
	
	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
}

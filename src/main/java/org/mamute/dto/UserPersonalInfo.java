package org.mamute.dto;

import static org.mamute.model.MarkDown.parse;
import static org.mamute.sanitizer.HtmlSanitizer.sanitize;
import static org.mamute.validators.UserPersonalInfoValidator.ABOUT_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.ABOUT_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.ABOUT_MIN_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_MIN_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static org.mamute.validators.UserPersonalInfoValidator.LOCATION_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.LOCATION_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.MARKED_ABOUT_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_REQUIRED;
import static org.mamute.validators.UserPersonalInfoValidator.REALNAME_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static org.mamute.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.model.User;

public class UserPersonalInfo {
	private User user;
	
	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
	@NotEmpty(message = NAME_REQUIRED)
	private String name;
	
	@Length(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;
	
	@Length(min = WEBSITE_MIN_LENGTH, max = WEBSITE_MAX_LENGHT, message = WEBSITE_LENGTH_MESSAGE)
	private String website;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String location;
	
	private DateTime birthDate;
	
	@Length(min = ABOUT_MIN_LENGTH, max = ABOUT_MAX_LENGTH ,  message = ABOUT_LENGTH_MESSAGE)
	private String about;

	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = REALNAME_LENGTH_MESSAGE)
	private String realName;

	@Length(min = ABOUT_MIN_LENGTH, max = MARKED_ABOUT_MAX_LENGTH ,  message = ABOUT_LENGTH_MESSAGE)
	private String markedAbout;

	private boolean isSubscribed;
	
	public UserPersonalInfo(User user) {
		this.user = user;
	}

	public UserPersonalInfo withBirthDate(DateTime birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	public UserPersonalInfo withLocation(String location) {
		this.location = sanitize(location);
		return this;
	}

	public UserPersonalInfo withWebsite(String website) {
		this.website = sanitize(website);
		return this;
	}

	public UserPersonalInfo withEmail(String email) {
		this.email = email;
		return this;
	}

	public UserPersonalInfo withAbout(String about) {
		this.about = about;
		this.markedAbout = sanitize(parse(about));
		return this;
	}

	public UserPersonalInfo withRealName(String realName) {
		this.realName = sanitize(realName);
		return this;
	}
	
	public UserPersonalInfo withName(String name){
		this.name = sanitize(name);
		return this;
	}
	
	public User getUser() {
		return this.user;
	}

	public String getName() {
		return this.name;
	}
	
	public String getRealName() {
		return this.realName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getWebsite() {
		return this.website;
	}

	public String getLocation() {
		return this.location;
	}

	public DateTime getBirthDate() {
		return this.birthDate;
	}

	public String getAbout() {
		return this.about;
	}
	
	public String getMarkedAbout() {
		return this.markedAbout;
	}

	public UserPersonalInfo withIsSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
		return this;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}
}
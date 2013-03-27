package br.com.caelum.brutal.dto;

import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.LOCATION_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.LOCATION_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_REQUIRED;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.REALNAME_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.User;

public class UserPersonalInfo {
	private User user;
	
	@NotEmpty(message = NAME_REQUIRED)
	@Length(max = NAME_MAX_LENGTH, message = REALNAME_LENGTH_MESSAGE)
	private String name;
	
	@Length(max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;
	
	@Length(min = WEBSITE_MIN_LENGTH, max = WEBSITE_MAX_LENGHT, message = WEBSITE_LENGTH_MESSAGE)
	private String website;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String location;
	
	private DateTime birthDate;
	
	@Length(min = ABOUT_MIN_LENGTH, max = ABOUT_MAX_LENGTH ,  message = ABOUT_LENGTH_MESSAGE)
	private String about;

	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
	private String realName;
	
	public UserPersonalInfo(User user, String name, String realName, String email,
			String website, String location, DateTime birthDate,
			String about) {
		this.user = user;
		this.name = name;
		this.realName = realName;
		this.email = email;
		this.website = website;
		this.location = location;
		this.birthDate = birthDate;
		this.about = about;
	}
	
	public User getUser() {
		return user;
	}

	public String getName() {
		return name;
	}
	
	public String getRealName() {
		return realName;
	}

	public String getEmail() {
		return email;
	}

	public String getWebsite() {
		return website;
	}

	public String getLocation() {
		return location;
	}

	public DateTime getBirthDate() {
		return birthDate;
	}

	public String getAbout() {
		return about;
	}



}

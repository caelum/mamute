package br.com.caelum.brutal.dto;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.User;

public class UserPersonalInfoBuilder {
	
	private String name = "default name";
	private String realName;
	private String email = "default@email.com";
	private String website;
	private DateTime birthDate;
	private String about;
	private User user = new User(name, email);
	private String location;

	public UserPersonalInfo build() {
		return new UserPersonalInfo(user, name, realName, email, website, location, birthDate, about);
	}
	
	public UserPersonalInfoBuilder withUser(User user){
		this.user = user;
		return this;
	}
	
	public UserPersonalInfoBuilder with(DateTime birthDate){
		this.birthDate = birthDate;
		return this;
	}
	
	public UserPersonalInfoBuilder withName(String name){
		this.name = name;
		return this;
	}
	
	public UserPersonalInfoBuilder withLocation(String location) {
		this.location = location;
		return this;
	}
	
	public UserPersonalInfoBuilder withRealName(String realName){
		this.realName = realName;
		return this;
	}
	
	public UserPersonalInfoBuilder withEmail(String email){
		this.email = email;
		return this;
	}
	
	public UserPersonalInfoBuilder withWebsite(String website){
		this.website = website;
		return this;
	}
	
	public UserPersonalInfoBuilder withAbout(String about){
		this.about = about;
		return this;
	}
}

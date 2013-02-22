package br.com.caelum.brutal.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import br.com.caelum.brutal.model.User;

public class UserPersonalInfoDTO {

	
	private User user;
	
	@NotEmpty
	@Length(min = 6, max = 100)
	private String name;
	
	@Length(max = 100)
	@Email
	private String email;
	
	@Length(max = 200)
	private String website;
	
	@Length(max = 100)
	private String location;
	
	private LocalDate birthDate;
	
	private String description;
	
	public UserPersonalInfoDTO(User user, String name, String email,
			String website, String location, LocalDate birthDate,
			String description) {
				this.user = user;
				this.name = name;
				this.email = email;
				this.website = website;
				this.location = location;
				this.birthDate = birthDate;
				this.description = description;
	}

	public User getUser() {
		return user;
	}

	public String getName() {
		return name;
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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getDescription() {
		return description;
	}



}

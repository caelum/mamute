package org.mamute.model;

import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
public class LoginMethod {
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * password or user's password
	 */
	private String token;
	
	@Length(max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String serviceEmail;
	
	@ManyToOne
	private User user;

	@Enumerated(EnumType.STRING)
	private MethodType type;

	/**
	 * @deprecated
	 */
	LoginMethod() {
	}

	public LoginMethod(MethodType type, String email, String password, User user) {
		this.type = type;
		this.serviceEmail = email;
		this.user = user;
		type.setPassword(this, password);
		
	}

	public void updateForgottenPassword(String password) {
		type.updateForgottenPassword(password, this);
	}

	void setToken(String token) {
		this.token = token;
	}
	
	public static LoginMethod brutalLogin(User user, String email, String password) {
		return new LoginMethod(MethodType.BRUTAL, email, password, user);
	}
	
	public static LoginMethod facebookLogin(User user, String email, String token) {
		return new LoginMethod(MethodType.FACEBOOK, email, token, user);
	}
	
	public static LoginMethod newLogin(User user, String email, String token, MethodType methodType) {
		return new LoginMethod(methodType, email, token, user);
	}

	public boolean isBrutal() {
		return type.equals(MethodType.BRUTAL);
	}
	
	public boolean isFacebook() {
		return type.equals(MethodType.FACEBOOK);
	}

	public String getToken() {
		return token;
	}


}

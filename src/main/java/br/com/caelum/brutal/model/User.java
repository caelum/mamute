package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.Years;

import br.com.caelum.brutal.infra.Digester;
import br.com.caelum.brutal.model.interfaces.Identifiable;
import br.com.caelum.brutal.model.interfaces.Moderatable;

@Table(name="Users")
@Entity
public class User implements Identifiable {

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();
	
	@Column(unique=true)
	@Length(min = EMAIL_MIN_LENGTH , max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty
	private String password = "";
	
	@NotEmpty
	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
	private String name;
	
	@Length(min = WEBSITE_MIN_LENGTH, max = WEBSITE_MAX_LENGHT, message = WEBSITE_LENGTH_MESSAGE)
	private String website;
	
	private String location;
	
	@Length(min = ABOUT_MIN_LENGTH, max = ABOUT_MAX_LENGTH ,  message = ABOUT_LENGTH_MESSAGE)
	private String about;
	
	private String markedAbout;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime birthDate;
	
	private long karma = 0;
	
	private boolean moderator = false;

	private String forgotPasswordToken = "";
	
	private String photoUri;

	@Type(type = "text")
	@NotEmpty
	private String sluggedName;

	static final User GHOST;
	static {
		GHOST = new User("", "", "");
		GHOST.setId(1000l);
	}
	
	/**
	 * @deprecated hibernate eyes only
	 */
	protected User() {
		this("", "", "786213675312678");
	}

	public User(String name, String email, String password) {
		super();
		this.email = email;
		setName(name);
		this.password = Digester.encrypt(password);
	}

	public void setName(String name) {
		this.name = name;
		this.sluggedName = toSlug(name);
	}

	public Long getId() {
		return id;
	}
	
	void setId(Long id) {
        this.id = id;
    }
	
	public String getSmallPhoto() {
	    return getPhoto("32x32");
	}
	
	public String getMediumPhoto() {
		return getPhoto("128x128");
	}
	
	public String getPhoto(String size) {
	    if (photoUri == null) {
	    		String digest = Digester.md5(email);
	    		String robohash = "http://robohash.org/size_"+size+"/"+digest+".png?size="+size+"&bgset=any";

	        try {
				return "http://www.gravatar.com/avatar/" + digest + ".png?size="+size+"&d=" + java.net.URLEncoder.encode(robohash, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return "http://www.gravatar.com/avatar/" + digest;
			}
	    }
	    return photoUri;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "[User " + email + ", "+ name +"]";
	}
	
	public long getKarma() {
		return karma;
	}

	void setKarma(long karma) {
        this.karma = karma;
    }

	public String getEmail() {
		return email;
	}

	public boolean isModerator() {
		return moderator;
	}

	public User asModerator() {
		this.moderator = true;
		return this;
	}

	public String touchForgotPasswordToken() {
		String tokenSource = Math.random() + System.currentTimeMillis() + getEmail() + getId();
		this.forgotPasswordToken = Digester.encrypt(tokenSource);
		return forgotPasswordToken;
	}

	public boolean isValidForgotPasswordToken(String token) {
		return this.forgotPasswordToken.equals(token);
	}

	public boolean updateForgottenPassword(String password,
			String password_confirmation) {
		if(password.equals(password_confirmation)) {
			this.password = br.com.caelum.brutal.infra.Digester.encrypt(password);
			touchForgotPasswordToken();
			return true;
		}
		return false;
	}

	public String getPassword() {
		return password;
	}
	
	public String getSluggedName() {
		return sluggedName;
	}
	
	public DateTime getCreatedAt() {
		return createdAt;
	}
	
	public UpdateStatus approve(Moderatable moderatable, Information approvedInfo) {
	    if (this.isModerator()) {
	        moderatable.approve(approvedInfo);
	        approvedInfo.moderate(this, UpdateStatus.APPROVED);
	    }
        return UpdateStatus.REFUSED;
	}
	
	public boolean isAuthorOf(Question question){
		return this.id == question.getAuthor().getId();  
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
	
	public Integer getAge() {
		DateTime now = new DateTime();
		if (birthDate == null){
			return null;
		}
		return Years.yearsBetween(birthDate, now).getYears();
	}
	
	public void setAbout(String content) {
		this.about = content;
		this.markedAbout = MarkDown.parse(content);
	}
	
	public String getAbout() {
		return about;
	}
	
	public String getMarkedAbout() {
		return markedAbout;
	}
	
	public void setPersonalInformation(String email, String name, String website, String location, DateTime birthDate, String about) {
		this.birthDate = birthDate;
		this.email = email;
		this.name = name;
		this.website = website;
		setAbout(about);
	}

    public void descreaseKarma(int value) {
        this.karma -= value;
    }

    public void increaseKarma(int value) {
        this.karma += value;
    }

    public void setPhotoUri(URL storedUri) {
        photoUri = storedUri.toString();
    }
}

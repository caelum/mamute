package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.LOCATION_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.LOCATION_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.MARKED_ABOUT_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_REQUIRED;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.Years;

import br.com.caelum.brutal.dto.UserPersonalInfo;
import br.com.caelum.brutal.infra.Digester;
import br.com.caelum.brutal.model.interfaces.Identifiable;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.sanitizer.HtmlSanitizer;

@Table(name="Users")
@Entity
public class User implements Identifiable {


	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();
	
	@Index(name="session_key")
	@Column(unique=true)
	private String sessionKey;

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = NAME_REQUIRED)
	@Length(max = NAME_MAX_LENGTH, min = NAME_MIN_LENGTH, message = NAME_LENGTH_MESSAGE)
	private String name;
	
	@Length(max = NAME_MAX_LENGTH, min = NAME_MIN_LENGTH, message = NAME_LENGTH_MESSAGE)
	private String realName;
	
	@Length(min = WEBSITE_MIN_LENGTH, max = WEBSITE_MAX_LENGHT, message = WEBSITE_LENGTH_MESSAGE)
	private String website;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String location;
	
	@Length(min = ABOUT_MIN_LENGTH, max = ABOUT_MAX_LENGTH ,  message = ABOUT_LENGTH_MESSAGE)
	private String about;
	
	@Length(min = ABOUT_MIN_LENGTH, max = MARKED_ABOUT_MAX_LENGTH ,  message = ABOUT_LENGTH_MESSAGE)
	private String markedAbout;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime birthDate;
	
	private long karma = 0;
	
	private boolean moderator = false;

	private boolean confirmedEmail = false;

	private String forgotPasswordToken = "";
	
	private String photoUri;
	
	@Type(type = "text")
	@NotEmpty
	private String sluggedName;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime nameLastTouchedAt;

	@OneToMany(mappedBy="user")
	private List<LoginMethod> loginMethods = new ArrayList<>();

	static final User GHOST;

	@Length(max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;

	static {
		GHOST = new User("", "");
		GHOST.setId(1000l);
	}
	
	/**
	 * @deprecated hibernate eyes only
	 */
	protected User() {
		this("", "");
	}

	public User(String name, String email) {
		super();
		this.email = email;
		setName(name);
	}
	
	public DateTime getNameLastTouchedAt() {
		return nameLastTouchedAt;
	}

	public void setSessionKey() {
	    Long currentTimeMillis = System.currentTimeMillis();
	    this.sessionKey = Digester.encrypt(currentTimeMillis.toString() + this.id.toString());
    }

    public void setName(String name) {
		this.name = name;
		this.sluggedName = toSlug(name);
		this.nameLastTouchedAt = new DateTime();
	}

	public Long getId() {
		return id;
	}
	
	void setId(Long id) {
        this.id = id;
    }
	
	public String getSmallPhoto() {
	    return getPhoto(32, 32);
	}
	
	public String getMediumPhoto() {
		return getPhoto(128, 128);
	}
	
	public String getPhoto(Integer width, Integer height) {
		String size = width + "x" + height;
	    if (photoUri == null) {
	    		String digest = Digester.md5(email);
	    		String robohash = "http://robohash.org/size_"+size+"/"+digest+".png?size="+size+"&bgset=any";
	    		String gravatar = "http://www.gravatar.com/avatar/" + digest + ".png?r=PG&size=" + size;
	        try {
				return gravatar + "&d=" + java.net.URLEncoder.encode(robohash, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return gravatar;
			}
	    }
	    return photoUri;
	}

	public String getName() {
		return name;
	}
	
	public String getRealName() {
		return realName;
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
			String passwordConfirmation) {
		if (!password.equals(passwordConfirmation)) {
			return false;
		}
		for (LoginMethod method : loginMethods) {
			method.updateForgottenPassword(password);
		}
		return true;
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
		this.markedAbout = content == null ? null : HtmlSanitizer.sanitize(MarkDown.parse(content));
	}
	
	public String getAbout() {
		return about;
	}
	
	public String getMarkedAbout() {
		return markedAbout;
	}
	
	public void setPersonalInformation(UserPersonalInfo info) {
		this.realName = info.getRealName();
		this.birthDate = info.getBirthDate();
		setName(info.getName());
		this.email = info.getEmail();
		this.website = info.getWebsite();
		this.location = info.getLocation();
		setAbout(info.getAbout());
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
    
    public String getSessionKey() {
        return sessionKey;
    }
    
    public void resetSession() {
        sessionKey = null;
    }
    
    public void confirmEmail(){
    	confirmedEmail = true;
	}

	public void add(LoginMethod brutalLogin) {
		loginMethods.add(brutalLogin);
	}

	public LoginMethod getBrutalLogin() {
		for (LoginMethod method : loginMethods) {
			if (method.isBrutal()) {
				return method;
			}
		}
		throw new IllegalStateException("this guy dont have a brutal login method!");
	}
}

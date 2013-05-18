package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;
import static br.com.caelum.brutal.sanitizer.HtmlSanitizer.sanitize;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.ABOUT_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.LOCATION_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.LOCATION_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.MARKED_ABOUT_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.NAME_REQUIRED;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.REALNAME_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static br.com.caelum.brutal.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.brutal.model.watch.Watcher;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="cache")
@Table(name="Users")
@Entity
public class User implements Identifiable {

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();
	
	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = NAME_REQUIRED)
	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
	private String name;
	
	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = REALNAME_LENGTH_MESSAGE)
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
	private final List<LoginMethod> loginMethods = new ArrayList<>();

	static final User GHOST;

	@Length(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;

	private boolean isSubscribed = true;
	
	@OneToMany(mappedBy = "watcher")
	private final List<Watcher> watches = new ArrayList<>();
	
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
		setName(name);
		this.email = email;
	}

	public DateTime getNameLastTouchedAt() {
		return nameLastTouchedAt;
	}

	public UserSession newSession() {
	    Long currentTimeMillis = System.currentTimeMillis();
	    String sessionKey = Digester.encrypt(currentTimeMillis.toString() + this.id.toString());
	    UserSession userSession = new UserSession(this, sessionKey);
	    return userSession;
    }

    public void setName(String name) {
		this.name = sanitize(name);
		this.sluggedName = sanitize(toSlug(name));
		this.nameLastTouchedAt = new DateTime();
	}

    public void setId(Long id) {
    	this.id = id;
    }
	
	public void setPhotoUri(URL storedUri) {
		photoUri = storedUri.toString();
	}
	
	public void setPersonalInformation(UserPersonalInfo info) {
		this.birthDate = info.getBirthDate();
		this.realName = info.getRealName();
		setName(info.getName());
		this.email = info.getEmail();
		this.website = info.getWebsite();
		this.location = info.getLocation();
		this.about = info.getAbout();
		this.markedAbout = info.getMarkedAbout();
		this.isSubscribed = info.isSubscribed();
	}

	void setKarma(long karma) {
        this.karma = karma;
    }

	public boolean isSubscribed() {
		return isSubscribed;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getSmallPhoto() {
	    return getPhoto(32, 32);
	}
	
	public String getRankingPhoto() {
		return getPhoto(50, 50);
	}
	
	public String getMediumPhoto() {
		return getPhoto(128, 128);
	}

	public String getName() {
		return name;
	}
	
	public String getRealName() {
		return realName;
	}

	public long getKarma() {
		return karma;
	}

	public String getEmail() {
		return email;
	}

	public String getSluggedName() {
		return sluggedName;
	}
	
	public DateTime getCreatedAt() {
		return createdAt;
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
	
	public LoginMethod getBrutalLogin() {
		for (LoginMethod method : loginMethods) {
			if (method.isBrutal()) {
				return method;
			}
		}
		throw new IllegalStateException("this guy dont have a brutal login method!");
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
	
	public String getAbout() {
		return about;
	}
	
	public String getMarkedAbout() {
		return markedAbout;
	}

	@Override
	public String toString() {
		return "[User " + email + ", "+ name +", "+ id +"]";
	}
	
	public boolean isModerator() {
		return moderator;
	}

	public User asModerator() {
		this.moderator = true;
		return this;
	}
	
	public void setSubscribed(boolean isSubscribed){
		this.isSubscribed = isSubscribed;
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

	public UpdateStatus approve(Moderatable moderatable, Information approvedInfo) {
	    if (this.isModerator()) {
	        moderatable.approve(approvedInfo);
	        approvedInfo.moderate(this, UpdateStatus.APPROVED);
	    }
        return UpdateStatus.REFUSED;
	}

	public void descreaseKarma(int value) {
        this.karma -= value;
    }

    public void increaseKarma(int value) {
        this.karma += value;
    }
    
    public void confirmEmail(){
    	confirmedEmail = true;
	}
    
	public void add(LoginMethod brutalLogin) {
		loginMethods.add(brutalLogin);
	}

	public boolean isAuthorOf(Votable votable) {
		return id == votable.getAuthor().getId();
	}

}

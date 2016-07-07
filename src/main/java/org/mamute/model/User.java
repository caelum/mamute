package org.mamute.model;

import static org.mamute.infra.Digester.hashFor;
import static org.mamute.infra.NormalizerBrutal.toSlug;
import static org.mamute.model.SanitizedText.fromTrustedText;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_MIN_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static org.mamute.validators.UserPersonalInfoValidator.LOCATION_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.LOCATION_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static org.mamute.validators.UserPersonalInfoValidator.NAME_REQUIRED;
import static org.mamute.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static org.mamute.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static org.mamute.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Vetoed;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.mamute.auth.rules.PermissionRules;
import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.dto.UserPersonalInfo;
import org.mamute.infra.Digester;
import org.mamute.model.interfaces.Identifiable;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.model.interfaces.Votable;
import org.mamute.model.watch.Watcher;
import org.mamute.providers.SessionFactoryCreator;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="cache")
@Table(name="Users")
@SQLDelete(sql = "update Users set deleted = true where id = ?")
@Where(clause = "deleted = 0")
@Entity
@Vetoed
public class User implements Identifiable {
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();
	
	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = NAME_REQUIRED)
	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
	private String name;
	
	@Length(min = WEBSITE_MIN_LENGTH, max = WEBSITE_MAX_LENGHT, message = WEBSITE_LENGTH_MESSAGE)
	private String website;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String location;
	
	private String about;
	
	private String markedAbout;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime birthDate;
	
	private long karma = 0;
	
	private boolean moderator = false;

	private boolean confirmedEmail = false;

	private String forgotPasswordToken = "";
	
	private String photoUri;
	
	@Type(type = "text")
	@NotEmpty
	private String sluggedName;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime nameLastTouchedAt;

	@OneToMany(mappedBy="user")
	private final List<LoginMethod> loginMethods = new ArrayList<>();

	public static final User GHOST;

	@Length(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;

	private boolean isSubscribed = true;
	
	private boolean isBanned = false;
	
	@OneToMany(mappedBy = "watcher")
	private final List<Watcher> watches = new ArrayList<>();
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime lastUpvote = new DateTime();

	@ManyToOne
	private Attachment avatarImage;

	private boolean receiveAllUpdates = false;

	private boolean deleted = false;

	static {
		GHOST = new User(fromTrustedText("GHOST"), "");
		GHOST.setId(1000l);
	}
	
	/**
	 * @deprecated hibernate eyes only
	 */
	protected User() {
		this(fromTrustedText(""), "");
	}

	public User(SanitizedText name, String email) {
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

    public void setName(SanitizedText name) {
		this.name = name.getText();
		this.sluggedName = toSlug(this.name);
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
		this.name = info.getName();
		this.email = info.getEmail();
		this.website = info.getWebsite();
		this.location = info.getLocation();
		this.about = info.getAbout();
		this.markedAbout = info.getMarkedAbout();
		this.isSubscribed = info.isSubscribed();
		this.receiveAllUpdates = info.getReceiveAllUpdates();
	}

	void setKarma(long karma) {
        this.karma = karma;
    }

	public boolean isSubscribedToNewsletter() {
		return isSubscribed;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getSmallPhoto(String gravatarUrl) {
	    return getPhoto(32, 32, gravatarUrl);
	}
	
	public String getMediumPhoto(String gravatarUrl) {
		return getPhoto(48, 48, gravatarUrl);
	}
	
	public String getBigPhoto(String gravatarUrl) {
		return getPhoto(128, 128, gravatarUrl);
	}

	public String getName() {
		return name;
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
	
	public String getPhoto(Integer width, Integer height, String gravatarUrl) {
		if (this.avatarImage != null) {
			return localAvatarPhoto(width, height);
		}
		String size = width + "x" + height;
		if (photoUri == null) {
			String digest = Digester.md5(email);
			String robohash = "https://robohash.org/size_"+size+"/set_set1/bgset_any/"+digest+".png";
			String gravatar = gravatarUrl + "/avatar/" + digest + ".png?r=PG&size=" + size;
			try {
				return gravatar + "&d=" + java.net.URLEncoder.encode(robohash, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return gravatar;
			}
		} else if (photoUri.contains("googleusercontent")) {
            return photoUri.replaceAll("sz=(\\d+)", "sz="+width);
        } else {
			return photoUri + "?width=" + width + "&height=" + height;
		}
	}

	private String localAvatarPhoto(Integer width, Integer height) {
		String format = "/attachments/%d?w=%d&h=%d";
		String url = String.format(format, avatarImage.getId(), width, height);
		return url;
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

	public User removeModerator() {
		this.moderator = false;
		return this;
	}
	
	public boolean canModerate(EnvironmentKarma environmentKarma) {
		long karma = environmentKarma.get(PermissionRules.MODERATE_EDITS);
		return isModerator() || this.karma >= karma;
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

	public UpdateStatus approve(Moderatable moderatable, Information approvedInfo, EnvironmentKarma environmentKarma) {
	    if (this.canModerate(environmentKarma)) {
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
	
	public boolean hasKarmaToAnswerOwnQuestion(EnvironmentKarma environmentKarma) {
		long answerOwnQuestion = environmentKarma.get(PermissionRules.ANSWER_OWN_QUESTION);
		return (this.karma >= answerOwnQuestion) || isModerator();
	}
	
	public boolean hasKarmaToAnswerInactiveQuestion(EnvironmentKarma environmentKarma) {
		long answerInactiveQuestion = environmentKarma.get(PermissionRules.INACTIVATE_QUESTION);
		return (this.karma >= answerInactiveQuestion) || isModerator();
	}
	
	public List<LoginMethod> getLoginMethods() {
		return loginMethods;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	public String getUnsubscribeHash() {
		return Digester.encrypt(this.email + hashFor(this.id));
	}
	
	public boolean isBanned() {
		return isBanned;
	}

	public void ban() {
		this.isBanned = true;
	}
	
	public void undoBan() {
		this.isBanned = false;
	}

	public void votedUp() {
		this.lastUpvote = new DateTime();
	}
	
	public boolean isVotingEnough(){
		return !lastUpvote.isBefore(new DateTime().minusWeeks(1));
	}

	public boolean hasKarma() {
		return (this.karma >= 0);
	}

	public void setAvatar(Attachment attachment) {
		this.avatarImage = attachment;
	}

	public Attachment getAvatar() {
		return avatarImage;
	}

	public boolean receiveAllUpdates() {
		return receiveAllUpdates;
	}
}

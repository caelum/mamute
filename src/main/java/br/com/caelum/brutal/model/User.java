package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.Years;

import br.com.caelum.brutal.infra.Digester;
import br.com.caelum.brutal.model.interfaces.Identifiable;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.model.interfaces.Updatable;

@Table(name="Users")
@Entity
public class User implements Identifiable {

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Column(unique=true)
	private String email;

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty
	@Length(min = 6)
	private String password = "";
	
	@NotEmpty
	@Length(min = 6)
	private String name;

	private String website;
	
	private String location;
	
	private String about;
	
	private String markedAbout;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime birthDate;
	
	private long karma = 0;
	
	private boolean moderator = false;

	private String forgotPasswordToken = "";

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
	
	public String getPhoto() {
		return "http://www.gravatar.com/avatar/" + Digester.md5(email);
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

    public UpdateStatus canUpdate(Updatable updatable) {
        User author = updatable.getAuthor();
        if (author.getId().equals(id) || this.isModerator()) {
            return UpdateStatus.NO_NEED_TO_APPROVE;
        }
        return UpdateStatus.PENDING;
    }

	public String touchForgotPasswordToken () {
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
	
	public int getAge() {
		DateTime now = new DateTime();
		if (birthDate == null){
			return 0;
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
		this.email = email;
		this.birthDate = birthDate;
		this.name = name;
		this.website = website;
		setAbout(about);
	}
}

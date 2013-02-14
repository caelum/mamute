package br.com.caelum.brutal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import br.com.caelum.brutal.infra.Digester;

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
	
	private long karma = 0;
	
	private boolean moderator = false;

	private String forgotPasswordToken = "";

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
		this.name = name;
		this.password = Digester.encrypt(password);
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
	
	public UpdateStatus approve(Updatable updatable, Information approvedInfo) {
	    if (this.canUpdate(updatable) == UpdateStatus.NO_NEED_TO_APPROVE) {
	        updatable.moderateCurrentInformation(this, UpdateStatus.EDITED);
	        updatable.approve(approvedInfo);
	        approvedInfo.moderate(this, UpdateStatus.APPROVED);
	    }
        return UpdateStatus.REFUSED;
	}
}

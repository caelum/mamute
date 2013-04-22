package br.com.caelum.brutal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class UserSession {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Index(name="session_key")
	@Column(unique=true)
	private String sessionKey;
	
	@ManyToOne
	private User user;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();
	
	/**
	 * @deprecated
	 */
	UserSession() {
	}

	public UserSession(User user, String sessionKey) {
		this.user = user;
		this.sessionKey = sessionKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}
	
	public User getUser() {
		return user;
	}
}

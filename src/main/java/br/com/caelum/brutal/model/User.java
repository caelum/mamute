package br.com.caelum.brutal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import br.com.caelum.brutal.infra.Digester;
import br.com.caelum.brutal.integracao.dao.Identifiable;

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
	private String password = "";
	
	@NotEmpty
	@Type(type = "text")
	private String name;
	
	private long karma = 0;
	
	private boolean moderator = false;
	
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

	public boolean isModerator() {
		return moderator;
	}

	public User asModerator() {
		this.moderator = true;
		return this;
	}
}

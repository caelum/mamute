package br.com.caelum.brutal.model;

import javax.persistence.Embedded;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class User {

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	private String email;

	@Id
	@GeneratedValue
	private Long id;

	private String password = "";

	/**
	 * @deprecated hibernate eyes only
	 */
	protected User() {
		this("", "786213675312678");
	}

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = Digester.encrypt(password);
	}
	
	@Override
	public String toString() {
		return "[User " + email + "]";
	}

}

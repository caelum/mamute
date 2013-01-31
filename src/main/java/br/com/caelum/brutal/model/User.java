package br.com.caelum.brutal.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import br.com.caelum.vraptor.mauth.Password;
import br.com.caelum.vraptor.mauth.SystemUser;
import br.com.caelum.vraptor.mauth.user.NavigationInfo;

@Entity
public class User {

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	private String email;

	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private String password = "";

	@Embedded
	private final NavigationInfo navigationInfo = new NavigationInfo();

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

}

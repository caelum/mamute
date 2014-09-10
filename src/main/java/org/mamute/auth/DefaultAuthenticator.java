package org.mamute.auth;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;
import org.mamute.dao.UserDAO;
import org.mamute.model.User;

public class DefaultAuthenticator {
	public static final String AUTH_CONFIG = "auth.type";
	public static final String LDAP_AUTH = "ldap";
	public static final String DB_AUTH = "db";

	@Inject
	private UserDAO users;
	@Inject
	private Access system;
	@Inject
	private LDAPApi ldap;
	@Inject
	private Environment env;

	public boolean authenticate(String email, String password) {
		//auth credentials
		if(!doAuthenticate(email, password)){
			return false;
		}

		User retrieved = users.findByEmail(email);
		if (retrieved == null) {
			retrieved = users.findByMailAndLegacyPasswordAndUpdatePassword(email, password);
		}
		if (retrieved == null) {
			return false;
		}

		system.login(retrieved);
		return true;
	}

	private boolean doAuthenticate(String email, String password) {
		String type = env.get(AUTH_CONFIG, DB_AUTH);
		if (type.equals(LDAP_AUTH)) {
			return ldap.authenticate(email, password);
		} else if (type.equals(DB_AUTH)) {
			return users.findByMailAndPassword(email, password) != null;
		} else {
			throw new RuntimeException("Unrecognized authentication type [" + type + "]");
		}
	}

	public void signout() {
		system.logout();
	}

}

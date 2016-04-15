package org.mamute.auth;

import org.mamute.dao.UserDAO;
import org.mamute.model.User;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDAPAuthenticator implements Authenticator{
	private static final Logger logger = LoggerFactory.getLogger(LDAPApi.class);

	@Inject private UserDAO users;
	@Inject private Access system;
	@Inject private LDAPApi ldap;

	public boolean authenticate(String username, String password) {
		//auth credentials
		if (!ldap.authenticate(username, password)) {
			return false;
		}

		String email = ldap.getEmail(username);
		User retrieved = users.findByEmail(email);
		if (retrieved == null) {
			retrieved = users.findByMailAndLegacyPasswordAndUpdatePassword(email, password);
		}
		if (retrieved == null) {
			logger.warn("unable to find user by email in the database: " + email);
			return false;
		}

		system.login(retrieved);
		return true;
	}

	public void signout() {
		system.logout();
	}
}

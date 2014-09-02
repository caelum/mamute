package org.mamute.auth;

import org.mamute.dao.UserDAO;
import org.mamute.model.User;

import javax.inject.Inject;

public class DbAuthenticator implements Authenticator {
	@Inject private UserDAO users;
	@Inject private Access system;

	public boolean authenticate(String email, String password) {
		User retrieved = users.findByMailAndPassword(email, password);
		if (retrieved == null) {
			retrieved = users.findByMailAndLegacyPasswordAndUpdatePassword(email, password);
		}
		if (retrieved == null) {
			return false;
		}

		system.login(retrieved);
		return true;
	}

	public void signout() {
		system.logout();
	}
}

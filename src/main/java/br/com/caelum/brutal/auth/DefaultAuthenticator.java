package br.com.caelum.brutal.auth;

import javax.inject.Inject;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;

public class DefaultAuthenticator {

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

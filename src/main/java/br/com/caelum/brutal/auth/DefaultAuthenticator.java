package br.com.caelum.brutal.auth;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Component
@RequestScoped
public class DefaultAuthenticator {

	private final UserDAO users;
	private final Access system;

	public DefaultAuthenticator(UserDAO users, Access system) {
		this.users = users;
		this.system = system;
	}

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

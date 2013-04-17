package br.com.caelum.brutal.dto;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.UserSession;

public class UserAndSession {

	private final User user;
	private final UserSession userSession;

	public UserAndSession(User user, UserSession userSession) {
		this.user = user;
		this.userSession = userSession;
	}

	public User getUser() {
		return user;
	}

	public UserSession getUserSession() {
		return userSession;
	}

}

package org.mamute.model;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Objects.firstNonNull;

public class LoggedUser {

	private final User user;
	private final HttpServletRequest request;

	public LoggedUser(User user, HttpServletRequest request) {
		this.user = user;
		this.request = request;
	}

	public User getCurrent() {
		return isLoggedIn() ? user : User.GHOST;
	}

	public String getIp() {
		if (request == null) {
			return null;
		}
		return firstNonNull(request.getHeader("X-Real-IP"), request.getRemoteAddr());
	}

	public boolean isModerator() {
		return isLoggedIn() ? user.isModerator() : false;
	}

	public boolean canModerate() {
		return isLoggedIn() ? user.canModerate() : false;
	}

	public boolean isLoggedIn() {
		return user != null;
	}

	@Override
	public String toString() {
		return "[user = " + user + ", ip = " + getIp() + "]";
	}


}

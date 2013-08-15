package br.com.caelum.brutal.model;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class LoggedUser {
	
	private User user;
	private HttpServletRequest request;

	@Deprecated
	public LoggedUser() {
	}

	@Inject
	public LoggedUser(@Nullable User user, HttpServletRequest request) {
		this.user = user;
		this.request = request;
	}
	
	public User getCurrent() {
		return isLoggedIn() ? user : User.GHOST;
	}

	public String getIp() {
		return request == null ? null : request.getRemoteAddr();
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

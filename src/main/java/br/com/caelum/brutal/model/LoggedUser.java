package br.com.caelum.brutal.model;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class LoggedUser {
	
	private final User user;
	private final HttpServletRequest request;

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

	public boolean isLoggedIn() {
		return user != null;
	}
	
	@Override
	public String toString() {
		return user.toString() + "Ip:"+getIp();
	}
	

}

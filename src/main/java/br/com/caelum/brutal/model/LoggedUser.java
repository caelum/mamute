package br.com.caelum.brutal.model;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class LoggedUser {
	
	private final User user;
	private final HttpServletRequest request;

	public LoggedUser(User user, HttpServletRequest request) {
		this.user = user;
		this.request = request;
	}
	
	public User getCurrent() {
		return user == null ? User.GHOST : user;
	}

	public String getIp() {
		return request == null ? null : request.getRemoteAddr();
	}
	
	public boolean isModerator() {
	    return user == null ? false : user.isModerator();
	}
	

}

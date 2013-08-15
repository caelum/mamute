package br.com.caelum.brutal.auth;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.dto.UserAndSession;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.UserSession;

@RequestScoped
public class Access {
	
	public static final String BRUTAL_SESSION = "brutal_session";
    private UserAndSession userAndSession;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private UserDAO users; 
    
    @Deprecated
    public Access() {
	}
	
    @Inject
	public Access(HttpServletResponse response, HttpServletRequest request, UserDAO users) {
	    this.response = response;
        this.request = request;
        this.users = users;
    }

	public User login(User user) {
		if(user.isBanned()) throw new BannedUserException();
		
	    UserSession newSession = user.newSession();
	    Cookie cookie = new Cookie(BRUTAL_SESSION, newSession.getSessionKey());
	    cookie.setPath("/");
	    cookie.setHttpOnly(true);
	    cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
		users.save(newSession);
		this.userAndSession = new UserAndSession(user, newSession);
		return user;
	}

	@RequestScoped
	@Produces
	public User getInstance() {
		User user = userAndSession == null ? null : userAndSession.getUser();
		return user;
	}
	
	public boolean tryToAutoLogin() {
	    String key = extractKeyFromCookies();
	    if (key != null) {
	    	this.userAndSession = users.findBySessionKey(key);
	    }
	    return this.userAndSession != null;
	}
	
	@PostConstruct
	public void initialize() {
		tryToAutoLogin();
	}
	
    private String extractKeyFromCookies() {
        Cookie[] cookiesArray = request.getCookies();
        if (cookiesArray != null) {
            List<Cookie> cookies = Arrays.asList(cookiesArray);
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(BRUTAL_SESSION)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

	public void logout() {
	    Cookie cookie = new Cookie(BRUTAL_SESSION, "");
	    cookie.setPath("/");
	    cookie.setMaxAge(-1);
	    response.addCookie(cookie);
	    users.delete(userAndSession.getUserSession());
		this.userAndSession = null;
	}
	

}
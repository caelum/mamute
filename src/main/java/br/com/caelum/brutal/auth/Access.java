package br.com.caelum.brutal.auth;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class Access implements ComponentFactory<User> {
	
	public static final String BRUTAL_SESSION = "brutal_session";
    private User user;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final UserDAO users; 
	
	public Access(HttpServletResponse response, HttpServletRequest request, UserDAO users) {
	    this.response = response;
        this.request = request;
        this.users = users;
    }

	public User login(User user) {
	    user.setSessionKey();
	    Cookie cookie = new Cookie(BRUTAL_SESSION, user.getSessionKey());
	    cookie.setPath("/");
	    cookie.setHttpOnly(true);
        response.addCookie(cookie);
		this.user = user;
		return user;
	}

	@Override
	public User getInstance() {
		return user;
	}
	
	@PostConstruct
	public boolean tryToAutoLogin() {
	    String key = extractKeyFromCookies();
	    if (key != null) {
	    	this.user = users.findBySessionKey(key);
	    }
	    return this.user != null;
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
	    user.resetSession();
		this.user = null;
	}
	

}

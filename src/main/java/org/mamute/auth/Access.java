package org.mamute.auth;

import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.dto.UserAndSession;
import org.mamute.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@RequestScoped
public class Access {
	public static final String BRUTAL_SESSION = "brutal_session";
	private static final Logger LOG = LoggerFactory.getLogger(Access.class);
    private UserAndSession userAndSession;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private UserDAO users;
	@Inject private LoginMethodDAO loginMethods;

    @Deprecated
    public Access() {
	}
	
    @Inject
	public Access(HttpServletResponse response, HttpServletRequest request, UserDAO users) {
	    this.response = response;
        this.request = request;
        this.users = users;
    }

	private static String getUsername(String email)
	{
		if (email == null)
		{
			return null;
		}
		int n = email.length();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < n; i++)
		{
			char ch = email.charAt(i);
			if (ch == '@')
			{
				return sb.toString();
			}
			else
			{
				sb.append(ch);
			}
		}
		LOG.error("unable to get username from " + email);
		return null;
	}

	// for debugging
	private static void dumpHeaders(HttpServletRequest request)
	{
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String headerName = headerNames.nextElement();
			LOG.info(String.format("%s %s", headerName, request.getHeader(headerName)));
		}
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

	@Produces
	public LoggedUser getInstance()
	{
		User user = userAndSession == null ? null : userAndSession.getUser();
		if (user == null)
		{
			String email = request.getHeader("X-Auth-Params-Email");
			if (email != null && !email.isEmpty())
			{
				user = users.findByEmail(email);
				if (user == null)
				{
					// auto register, create entry for this user in the db with random password.
					//  The code here parallels the code in SignUpController
					String username = getUsername(email);
					if (username != null && !username.isEmpty())
					{
						String password = RandomString.generate(8);
						user = new User(SanitizedText.fromTrustedText(username), email);
						LoginMethod brutalLogin = LoginMethod.brutalLogin(user, email, password);
						user.add(brutalLogin);
						users.save(user);
						loginMethods.save(brutalLogin);
						LOG.info(String.format("successfully registered %s", username));
					}
				}
				if (user != null)
				{
					this.login(user);
				}
			}
		}
		return new LoggedUser(user, request);
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
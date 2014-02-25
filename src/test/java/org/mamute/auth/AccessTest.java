package org.mamute.auth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mamute.auth.Access;
import org.mamute.dao.TestCase;
import org.mamute.dao.UserDAO;
import org.mamute.dto.UserAndSession;
import org.mamute.model.User;
import org.mamute.model.UserSession;

public class AccessTest extends TestCase {
	
	private User user;
	private HttpServletResponse response;
	private HttpServletRequest request;
	private UserDAO users;

	@Before
	public void setup() {
		user = user("name", "email");
		response = mock(HttpServletResponse.class);
		request = mock(HttpServletRequest.class);
		users = mock(UserDAO.class);
	}

	@Test
	public void should_auto_login_with_valid_cookie() {
		String sessionKey = "session-key";
		Cookie brutalCookie = new Cookie(Access.BRUTAL_SESSION, sessionKey);
		Cookie[] cookies = new Cookie[]{anyCookie(), anyCookie(), brutalCookie, anyCookie(), anyCookie()};
		
		when(request.getCookies()).thenReturn(cookies);
		when(users.findBySessionKey(sessionKey)).thenReturn(new UserAndSession(user, new UserSession(user, sessionKey)));
		
		Access access = new Access(response, request, users);
		
		assertTrue(access.tryToAutoLogin());
	}
	
	@Test
	public void should_not_auto_login_without_brutal_cookie() {
		Cookie[] cookies = new Cookie[]{anyCookie(), anyCookie(), anyCookie(), anyCookie()};
		
		when(request.getCookies()).thenReturn(cookies);
		
		Access access = new Access(response, request, users);
		
		assertFalse(access.tryToAutoLogin());
	}
	
	@Test
	public void should_not_auto_login_with_invalid_session() {
		String sessionKey = "invalid";
		Cookie brutalCookie = new Cookie(Access.BRUTAL_SESSION, sessionKey);
		Cookie[] cookies = new Cookie[]{anyCookie(), brutalCookie, anyCookie()};
		
		when(request.getCookies()).thenReturn(cookies);
		when(users.findBySessionKey(sessionKey)).thenReturn(null);
		
		Access access = new Access(response, request, users);
		
		assertFalse(access.tryToAutoLogin());
	}

	private Cookie anyCookie() {
		String anyKey =  "anyKey" + new Random().nextInt(); 
		int rand = new Random().nextInt();
		return new Cookie(anyKey, rand + "");
	}

}

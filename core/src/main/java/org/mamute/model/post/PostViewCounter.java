package org.mamute.model.post;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Period;
import org.mamute.infra.Digester;
import org.mamute.model.interfaces.ViewCountable;

public class PostViewCounter {
	
	private HttpServletRequest request;
	private HttpServletResponse response;

	@Deprecated
	public PostViewCounter() {
	}
	
	@Inject
	public PostViewCounter(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public void ping(ViewCountable  post) {
		if (!recentlyViewed(post)) {
			post.ping();
			Cookie cookie = new Cookie(cookieKeyFor(post), "1");
			cookie.setHttpOnly(true);
			String path = request.getPathInfo();
			cookie.setPath(path);
				
			cookie.setMaxAge(Period.days(1).toStandardSeconds().getSeconds());
			response.addCookie(cookie);
		}
	}
	
	private boolean recentlyViewed(ViewCountable  post) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieKeyFor(post))) {
				return true;
			}
		}
		return false;
	}
	
	
	public String cookieKeyFor(ViewCountable  post) {
		String cookiePrefix = Digester.md5(post.getClass().getSimpleName());
		String cookieKey = cookiePrefix + "-" + post.getId();
		return cookieKey;
	}

}

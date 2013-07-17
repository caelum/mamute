package br.com.caelum.brutal.model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Period;

import br.com.caelum.brutal.infra.Digester;
import br.com.caelum.brutal.model.interfaces.ViewCountable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class PostViewCounter {
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;

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
	
	
	String cookieKeyFor(ViewCountable  post) {
		String cookiePrefix = Digester.md5(post.getClass().getSimpleName());
		String cookieKey = cookiePrefix + "-" + post.getId();
		return cookieKey;
	}

}

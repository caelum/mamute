package br.com.caelum.brutal.model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestionViewCounter {
	
	static final String COOKIE_PREFIX = "VIEWED_QUESTION_";
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public QuestionViewCounter(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public void ping(Question question) {
		if (!recentlyViewed(question)) {
			question.ping();
			Cookie cookie = new Cookie(COOKIE_PREFIX + question.getId(), "1");
			response.addCookie(cookie);
		}
	}
	
	private boolean recentlyViewed(Question question) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(COOKIE_PREFIX + question.getId())) {
				return true;
			}
		}
		return false;
	}
	

}

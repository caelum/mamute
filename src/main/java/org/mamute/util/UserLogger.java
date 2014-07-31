package org.mamute.util;

import br.com.caelum.vraptor.events.MethodReady;
import org.apache.log4j.Logger;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;

public class UserLogger {

	@Inject
	private LoggedUser loggedUser;
	@Inject
	private HttpServletRequest request;

	private static Logger LOG = Logger.getLogger(UserLogger.class);

	public void logUser(@Observes MethodReady methodReady) {
		if (loggedUser.isLoggedIn()) {
			String realIp = request.getHeader("X-Real-IP");
			User user = loggedUser.getCurrent();
			String name = user.getName();
			String id = user.getId().toString();
			String uri = request.getRequestURI();
			String method = request.getMethod();
			String log = format("%s -> %s from %s (id=%s) %s", method, uri, name, id,  realIp);
			LOG.info(log);
		}

	}
}

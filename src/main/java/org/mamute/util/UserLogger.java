package org.mamute.util;

import br.com.caelum.vraptor.events.MethodReady;
import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.mamute.infra.ClientIp;
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
	@Inject
	private ClientIp clientIp;

	private static Logger LOG = Logger.getLogger(UserLogger.class);

	public void logUser(@Observes MethodReady methodReady) {
		String userName = "anonymous";
		if (loggedUser.isLoggedIn()) {
			User user = loggedUser.getCurrent();
			String name = user.getName();
			String id = user.getId().toString();
			userName = format("%s (id=%s)", name, id);
		}

		String realIp = clientIp.get();
		String uri = request.getRequestURI();
		String method = getMethod();
		String log = format("%s -> %s from %s %s", method, uri, userName,  realIp);
		LOG.info(log);

	}

	private String getMethod() {
		String forcedMethod = request.getParameter("_method");
		return Objects.firstNonNull(forcedMethod, request.getMethod());
	}
}

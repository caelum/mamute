package org.mamute.auth;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.mamute.dao.UserDAO;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.MethodReady;

public class LdapSSOFilter {

	@Inject private Environment env;
	@Inject private UserDAO users;
	@Inject private LDAPApi ldap;
	@Inject private LoggedUser loggedUser;
	@Inject private HttpServletRequest request;
	@Inject private Access system;

	public void checkSSO(@Observes MethodReady methodReady) {
		if (env.supports(LDAPApi.LDAP_SSO) && !loggedUser.isLoggedIn() && request.getRequestURI() != null
				&& !request.getRequestURI().endsWith("/logout")) {
			String userName = request.getRemoteUser();

			if (userName != null && ldap.authenticateSSO(userName)) {
				String email = ldap.getEmail(userName);
				User retrieved = users.findByEmail(email);

				if (retrieved != null) {
					system.login(retrieved);
				}
			}
		}
	}

}

package org.mamute.providers;

import br.com.caelum.vraptor.environment.Environment;
import org.mamute.auth.Authenticator;
import org.mamute.auth.DbAuthenticator;
import org.mamute.auth.LDAPAuthenticator;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class AuthenticatorProvider {

	private final DbAuthenticator dbAuthenticator;
	private final LDAPAuthenticator ldapAuthenticator;
	private final Environment env;

	@Inject
	public AuthenticatorProvider(Environment env, DbAuthenticator dbAuthenticator, LDAPAuthenticator ldapAuthenticator) {
		this.env = env;
		this.dbAuthenticator = dbAuthenticator;
		this.ldapAuthenticator = ldapAuthenticator;
	}

	@Produces
	@RequestScoped
	public Authenticator getInstance() {
		if (env.supports("feature.auth.db")) {
			return dbAuthenticator;
		} else if (env.supports("feature.auth.ldap")) {
			return ldapAuthenticator;
		} else {
			throw new IllegalArgumentException("No supported auth feature enabled");
		}
	}
}
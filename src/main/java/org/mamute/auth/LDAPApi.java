package org.mamute.auth;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.mamute.auth.DefaultAuthenticator.AUTH_CONFIG;
import static org.mamute.auth.DefaultAuthenticator.LDAP_AUTH;
import static org.mamute.model.SanitizedText.pure;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.model.LoginMethod;
import org.mamute.model.SanitizedText;
import org.mamute.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;

public class LDAPApi {
	private Logger logger = LoggerFactory.getLogger(LDAPApi.class);

	public static final String LDAP_HOST = "ldap.host";
	public static final String LDAP_PORT = "ldap.port";
	public static final String LDAP_USER = "ldap.user";
	public static final String LDAP_PASS = "ldap.pass";
	public static final String LDAP_EMAIL = "ldap.emailAttr";
	public static final String LDAP_NAME = "ldap.nameAttr";
	public static final String LDAP_SURNAME = "ldap.surnameAttr";
	public static final String LDAP_USER_DN = "ldap.userDn";
	public static final String PLACHOLDER_PASSWORD = "ldap-password-ignore-me";

	@Inject private Environment env;
	@Inject private UserDAO users;
	@Inject private LoginMethodDAO loginMethods;

	private String host;
	private Integer port;
	private String user;
	private String pass;
	private String userDn;
	private String emailAttr;
	private String nameAttr;
	private String surnameAttr;

	/**
	 * Ensure that required variables are set if LDAP auth
	 * is enabled
	 */
	@PostConstruct
	public void init() {
		if (env.get(AUTH_CONFIG, "").equals(LDAP_AUTH)) {
			host = assertValuePresent(LDAP_HOST);
			port = Integer.parseInt(assertValuePresent(LDAP_PORT));
			user = assertValuePresent(LDAP_USER);
			pass = assertValuePresent(LDAP_PASS);
			userDn = assertValuePresent(LDAP_USER_DN);

			emailAttr = assertValuePresent(LDAP_EMAIL);
			nameAttr = assertValuePresent(LDAP_NAME);
			surnameAttr = env.get(LDAP_SURNAME, "");
		}
	}

	/**
	 * Attempt to authenticate against LDAP directory. Accepts email addresses
	 * as well as plain usernames; emails will have the '@mail.com' portion
	 * stripped off before read.
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean authenticate(String username, String password) {
		try (LDAPResource ldap = new LDAPResource()) {
			String formattedUser = username.replaceAll("@.+", "");
			String cn = "cn=" + formattedUser + "," + userDn;

			ldap.verifyCredentials(cn, password);
			createUserIfNeeded(ldap, cn);

			return true;
		} catch (LdapAuthenticationException e) {
			logger.debug("LDAP auth attempt failed");
			return false;
		} catch (LdapException | IOException e) {
			logger.debug("LDAP connection error", e);
			throw new AuthAPIException(LDAP_AUTH, "LDAP connection error", e);
		}
	}

	private void createUserIfNeeded(LDAPResource ldap, String cn) throws LdapException {
		Entry entry = ldap.getUser(cn);
		String email = ldap.getAttribute(entry, emailAttr);
		if (users.findByEmail(email) == null) {
			String fullName = ldap.getAttribute(entry, nameAttr);
			if (isNotEmpty(surnameAttr)) {
				fullName += " " + ldap.getAttribute(entry, surnameAttr);
			}

			User user = new User(pure(fullName.trim()), email);

			LoginMethod brutalLogin = LoginMethod.brutalLogin(user, email, PLACHOLDER_PASSWORD);
			user.add(brutalLogin);

			users.save(user);
			loginMethods.save(brutalLogin);
		}
	}

	private String assertValuePresent(String field) {
		String value = env.get(field, "");
		if (isEmpty(value)) {
			throw new RuntimeException("Field [" + field + "] is required when using LDAP authentication");
		}
		return value;
	}

	/**
	 * Acts as a session-level LDAP connection
	 */
	private class LDAPResource implements AutoCloseable {
		LdapConnection connection;

		private LDAPResource() throws LdapException {
			connection = connection(user, pass);
		}

		private void verifyCredentials(String userCn, String password) throws LdapException, IOException {
			try (LdapConnection conn = connection(userCn, password)) {
				logger.debug("LDAP login from [" + userCn + "]");
			}
		}

		private LdapConnection connection(String username, String password) throws LdapException {
			LdapNetworkConnection conn = new LdapNetworkConnection(host, port);
			conn.bind(username, password);
			return conn;
		}

		private Entry getUser(String cn) throws LdapException {
			return connection.lookup(cn);
		}

		private String getAttribute(Entry entry, String attribute) throws LdapException {
			return entry.get(attribute).getString();
		}

		@Override
		public void close() throws IOException {
			connection.close();
		}
	}
}

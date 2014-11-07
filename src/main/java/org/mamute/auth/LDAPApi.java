package org.mamute.auth;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.mamute.model.SanitizedText.fromTrustedText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.model.LoginMethod;
import org.mamute.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;

/**
 * LDAP authentication API
 */
public class LDAPApi {
	private static final Logger logger = LoggerFactory.getLogger(LDAPApi.class);

	public static final String LDAP_AUTH = "ldap";
	public static final String LDAP_HOST = "ldap.host";
	public static final String LDAP_PORT = "ldap.port";
	public static final String LDAP_USER = "ldap.user";
	public static final String LDAP_PASS = "ldap.pass";
	public static final String LDAP_USER_DN = "ldap.userDn";
	public static final String LDAP_EMAIL = "ldap.emailAttr";
	public static final String LDAP_NAME = "ldap.nameAttr";
	public static final String LDAP_SURNAME = "ldap.surnameAttr";
	public static final String LDAP_GROUP = "ldap.groupAttr";
	public static final String LDAP_MODERATOR_GROUP = "ldap.moderatorGroup";
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
	private String groupAttr;
	private String moderatorGroup;

	/**
	 * Ensure that required variables are set if LDAP auth
	 * is enabled
	 */
	@PostConstruct
	public void init() {
		if (env.supports("feature.auth.ldap")) {
			//required
			host = assertValuePresent(LDAP_HOST);
			port = Integer.parseInt(assertValuePresent(LDAP_PORT));
			user = assertValuePresent(LDAP_USER);
			pass = assertValuePresent(LDAP_PASS);
			userDn = assertValuePresent(LDAP_USER_DN);
			emailAttr = assertValuePresent(LDAP_EMAIL);
			nameAttr = assertValuePresent(LDAP_NAME);

			//optional
			surnameAttr = env.get(LDAP_SURNAME, "");
			groupAttr = env.get(LDAP_GROUP, "");
			moderatorGroup = env.get(LDAP_MODERATOR_GROUP, "");
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
			String cn = userCn(username);
			ldap.verifyCredentials(cn, password);
			createUserIfNeeded(ldap, cn);

			return true;
		} catch (LdapAuthenticationException e) {
			logger.debug("LDAP auth attempt failed");
			return false;
		} catch (LdapException | IOException e) {
			logger.debug("LDAP connection error", e);
			throw new AuthenticationException(LDAP_AUTH, "LDAP connection error", e);
		}
	}

	/**
	 * Find the email address for a given username
	 *
	 * @param username
	 * @return
	 */
	public String getEmail(String username) {
		try (LDAPResource ldap = new LDAPResource()) {
			Entry ldapUser = ldap.getUser(userCn(username));
			return ldap.getAttribute(ldapUser, emailAttr);
		} catch (LdapException | IOException e) {
			logger.debug("LDAP connection error", e);
			throw new AuthenticationException(LDAP_AUTH, "LDAP connection error", e);
		}
	}

	private String userCn(String username) {
		if (username.contains("@")) {
			// find user by email
			try (LDAPResource ldap = new LDAPResource()) {
				Entry user = ldap.findUserByEmail(username);
				if (user != null) {
					return user.getDn().getName();
				}
			} catch (LdapException | IOException e) {
				logger.debug("LDAP connection error", e);
				throw new AuthenticationException(LDAP_AUTH, "LDAP connection error", e);
			}
		}

		String sanitizedUser = username.replaceAll("[,=]", "");
		String cn = "cn=" + sanitizedUser + "," + userDn;
		return cn;
	}

	private void createUserIfNeeded(LDAPResource ldap, String cn) throws LdapException {
		Entry ldapUser = ldap.getUser(cn);
		String email = ldap.getAttribute(ldapUser, emailAttr);
		User user = users.findByEmail(email);
		if (user == null) {
			String fullName = ldap.getAttribute(ldapUser, nameAttr);
			if (isNotEmpty(surnameAttr)) {
				fullName += " " + ldap.getAttribute(ldapUser, surnameAttr);
			}

			user = new User(fromTrustedText(fullName.trim()), email);


			LoginMethod brutalLogin = LoginMethod.brutalLogin(user, email, PLACHOLDER_PASSWORD);
			user.add(brutalLogin);

			users.save(user);
			loginMethods.save(brutalLogin);
		}

		//update moderator status
		if (isNotEmpty(moderatorGroup) && ldap.getGroups(ldapUser).contains(moderatorGroup)) {
			user = user.asModerator();
		} else {
			user.removeModerator();
		}

		users.save(user);
	}

	private String assertValuePresent(String field) {
		String value = env.get(field, "");
		if (isEmpty(value)) {
			throw new RuntimeException("Field [" + field + "] is required when using LDAP authentication");
		}
		return value;
	}

	/**
	 * Acts as a session-level LDAP connection.
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

		private List<String> getGroups(Entry user) {
			List<String> groupCns = new ArrayList<>();
			if (isNotEmpty(groupAttr)) {
				for (Value grp : user.get(groupAttr)) {
					groupCns.add(grp.getString());
				}
			}
			return groupCns;
		}

		private Entry getUser(String cn) throws LdapException {
			return connection.lookup(cn);
		}

		private Entry findUserByEmail(String email) throws LdapException {
			EntryCursor responseCursor = connection.search(userDn, "(&(objectclass=user)(" + emailAttr + "=" + email + "))", SearchScope.SUBTREE);
			try {
				try {
					if (responseCursor != null && responseCursor.next()) {
						return responseCursor.get();
					}
				} catch (CursorException e) {
					logger.debug("LDAP search error", e);
					return null;
				}
			} finally {
				responseCursor.close();
			}
			return null;
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

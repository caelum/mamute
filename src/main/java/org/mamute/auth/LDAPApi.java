package org.mamute.auth;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.mamute.model.SanitizedText.fromTrustedText;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.naming.directory.InvalidAttributeValueException;

import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.filesystem.ImageStore;
import org.mamute.infra.ClientIp;
import org.mamute.model.Attachment;
import org.mamute.model.LoginMethod;
import org.mamute.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.observer.upload.DefaultUploadedFile;

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
	public static final String LDAP_USER_OBJECTCLASS = "ldap.userObjectClass";
	public static final String LDAP_LOOKUP = "ldap.lookupAttr";
	public static final String LDAP_MODERATOR_GROUP = "ldap.moderatorGroup";
	public static final String LDAP_LOOKUP_ALL_ATTR = "ldap.lookupAllAttr";
	public static final String LDAP_SSO = "ldap.sso";
	public static final String PLACHOLDER_PASSWORD = "ldap-password-ignore-me";
	public static final String LDAP_USE_SSL = "ldap.useSSL";
	public static final String LDAP_USE_TLS = "ldap.useTLS";
	public static final String LDAP_AVATAR_IMAGE = "ldap.avatarImageAttr";

	@Inject private Environment env;
	@Inject private UserDAO users;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private ImageStore imageStore;
	@Inject private ClientIp clientIp;


	private String host;
	private Integer port;
	private String user;
	private String pass;
	private String userDn;
	private String emailAttr;
	private String nameAttr;
	private String surnameAttr;
	private String groupAttr;
	private String[] lookupAttrs;
	private String userObjectClass;
	private String moderatorGroup;
	
	/**
	 * If set to true, then all attributes are pulled for the LDAP entry associated with the user.
	 * This uses the <code>SchemaConstants.ALL_ATTRIBUTES_ARRAY</code> constant. If false, the 
	 * normal lookup is performed, which will bring back user attributes but not necessarily
	 * operational attributes from the LDAP server.
	 */
	private Boolean lookupAllAttr;
	private Boolean useSsl;
	
	/**
	 * Note that TLS (STARTTLS) is different from SSL for LDAP authentication. TLS is the 
	 * preferred method of securing communucation with an LDAP server, rather than SSL (ldaps://)
	 */
	private Boolean useTls;
	private String avatarImageAttr;

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
			lookupAllAttr = env.supports(LDAP_LOOKUP_ALL_ATTR);
			lookupAttrs = env.get(LDAP_LOOKUP, "").split(",");
			userObjectClass = env.get(LDAP_USER_OBJECTCLASS, "user");
			useSsl = env.supports(LDAP_USE_SSL);
			useTls = env.supports(LDAP_USE_TLS);
			avatarImageAttr = env.get(LDAP_AVATAR_IMAGE, "");
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
	 * Authenticates a user WITHOUT a password presented. The user name must
	 * come from a trusted SSO source, e.g. the <code>getRemoteUser()</code>
	 * from the HTTP Servlet Request.
	 * 
	 * @param username
	 *            User name to authenticate in any case. If no matching user
	 *            account exists, it is created.
	 * 
	 * @return <code>true</code> if the user could be found in LDAP and the User
	 *         object was initialized, <code>false</code> otherwise.
	 */
	public boolean authenticateSSO(String username) {

		try (LDAPResource ldap = new LDAPResource()) {
			if (ldap.lookupUser(username) == null) {
				return false;
			}
			String cn = userCn(username);
			createUserIfNeeded(ldap, cn);

			return true;
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
		if (lookupAttrs.length > 0) {
			try (LDAPResource ldap = new LDAPResource()) {
				Entry user = ldap.lookupUser(username);
				if (user != null) {
					return user.getDn().getName();
				}
			} catch (LdapException | IOException e) {
				logger.debug("LDAP connection error", e);
				throw new AuthenticationException(LDAP_AUTH, "LDAP connection error", e);
			}
		}

		// fallback: assume lookup by CN
		String sanitizedUser = username.replaceAll("[,=]", "");
		String cn = "cn=" + sanitizedUser + "," + userDn;
		return cn;
	}

	private void updateAvatarImage(LDAPResource ldap, Entry entry, User user) {
		try {
			byte[] jpegBytes = getAvatarImage(ldap, entry);
			if (jpegBytes != null) {
				String fileName = user.getEmail() + ".jpg";
				DefaultUploadedFile avatar = new DefaultUploadedFile(new ByteArrayInputStream(jpegBytes), fileName, "image/jpeg", jpegBytes.length);
				Attachment attachment = imageStore.processAndStore(avatar, user, clientIp);
				Attachment old = user.getAvatar();
				if (old != null) {
					imageStore.delete(old);
				}
				user.setAvatar(attachment);
			}
		} catch (LdapException | IOException e) {
			// problems with avatar processing are non-fatal
			logger.warn("Error updating user avatar from LDAP: " + user.getName(), e);
		}
	}

	private byte[] getAvatarImage(LDAPResource ldap, Entry entry) throws LdapException {
		if (avatarImageAttr != null && avatarImageAttr.length() > 0) {
			try {
				return ldap.getByteAttribute(entry, avatarImageAttr);
			} catch (InvalidAttributeValueException ex) {
				throw new LdapException("Invalid attribute value while looking up " + avatarImageAttr, ex);
			}
		}
		return null;
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
		updateAvatarImage(ldap, ldapUser, user);

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
			// Manually build the configuration since the convenience constructor in 
			// the LdapNetworkConnection doesn't let us specify a TLS setting			
			LdapConnectionConfig config = new LdapConnectionConfig();
			config.setLdapHost(host);
			config.setLdapPort(port);
			config.setUseTls(useTls);
			config.setUseSsl(useSsl);
	        LdapNetworkConnection conn = new LdapNetworkConnection(config);
			
			conn.bind(username, password);
			return conn;
		}

		private List<String> getGroups(Entry user) {
			List<String> groupCns = new ArrayList<>();
			if (isNotEmpty(groupAttr)) {
				Attribute grpEntry = user.get(groupAttr);
				if (grpEntry != null) {
					for (Value<?> grp : grpEntry) {
						groupCns.add(grp.getString());
					}
				}
			}
			return groupCns;
		}

		private Entry getUser(String cn) throws LdapException {
			if (lookupAllAttr) {
				return connection.lookup(cn, SchemaConstants.ALL_ATTRIBUTES_ARRAY);
			} else {
				return connection.lookup(cn);
			}
		}

		private Entry lookupUser(String username) throws LdapException {
			StringBuilder userQuery = new StringBuilder();
			userQuery.append("(&(objectclass=");
			userQuery.append(userObjectClass);
			userQuery.append(")(|");
			boolean hasCondition = false;
			for (String lookupAttr : lookupAttrs) {
				String attrName = lookupAttr.trim();
				if (!attrName.isEmpty()) {
					userQuery.append('(').append(attrName).append('=').append(username).append(')');
					hasCondition = true;
				}
			}
			userQuery.append("))");

			if (!hasCondition) {
				return null;
			}

			logger.debug("LDAP user query " + userQuery.toString());

			EntryCursor responseCursor = connection.search(userDn, userQuery.toString(), SearchScope.SUBTREE);
			try {
				try {
					if (responseCursor != null && responseCursor.next()) {
						Entry match = responseCursor.get();
						logger.debug("LDAP user query result: " + match.getDn());
						return match;
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
			Attribute attr = entry.get(attribute);
			return attr == null ? null : attr.getString();
		}

		private byte[] getByteAttribute(Entry entry, String attribute) throws LdapException, InvalidAttributeValueException {
			Attribute value = entry.get(attribute);
			if (value != null) {
				return value.getBytes();
			}
			return null;
		}

		@Override
		public void close() throws IOException {
			connection.close();
		}
	}
}

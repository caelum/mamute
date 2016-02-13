package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.vidageek.mirror.dsl.Mirror;

import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.UserDAO;
import org.mamute.infra.Digester;
import org.mamute.infra.MD5;
import org.mamute.model.LoginMethod;
import org.mamute.model.MethodType;
import org.mamute.model.User;
import org.mamute.model.UserSession;

public class UserDAOTest extends DatabaseTestCase {

	private UserDAO users;

	@Before
	public void setup() {
		this.users = new UserDAO(session);
	}
	
	@Test
	public void should_search_by_email_and_password() {
		User gui = user("Guilherme Sonar", "gui@email.com.br");
		LoginMethod brutalLogin = LoginMethod.brutalLogin(gui, "gui@email.com.br", "654321");
		gui.add(brutalLogin);
		
		users.save(gui);
		session.save(brutalLogin);
		
		assertEquals(gui, users.findByMailAndPassword("gui@email.com.br", "654321"));
		assertNull(users.findByMailAndPassword("gui@email.com.br", "1234567"));
		assertNull(users.findByMailAndPassword("joao.silveira@email.com.br", "654321"));
	}
	
	@Test
	public void should_find_by_session_key() {
	    User gui = user("Guilherme Sonar", "gui@email.com.br");
	    users.save(gui);
	    UserSession userSession = gui.newSession();
	    users.save(userSession);
	    
	    assertEquals(gui, users.findBySessionKey(userSession.getSessionKey()).getUser());
	    assertNull(users.findBySessionKey("12345"));
	    assertNull(users.findBySessionKey(null));
	}
	
	@Test
	public void should_search_by_email_and_legacy_password_and_update_password() {
	    String password = "654321";
        User gui = user("Guilherme Sonar", "gui@email.com.br");
		LoginMethod brutalLogin = LoginMethod.brutalLogin(gui, "gui@email.com.br", password);
	    new Mirror().on(brutalLogin).set().field("token").withValue(MD5.crypt(password));

	    gui.add(brutalLogin);
	    users.save(gui);
	    session.save(brutalLogin);
	    
	    assertNull(users.findByMailAndPassword("gui@email.com.br", password));
	    
	    User found = users.findByMailAndLegacyPasswordAndUpdatePassword("gui@email.com.br", password);
	    
        assertEquals(gui, found);
	    assertEquals(Digester.encrypt(password), found.getBrutalLogin().getToken());
	    
	    assertNull(users.findByMailAndPassword("joao.silveira@email.com.br", password));
	    assertNull(users.findByMailAndPassword("gui@email.com.br", "123456"));
	}
	
	@Test
	public void should_find_by_email_and_login_method() throws Exception {
		User user = saveUser("chico@brutal.com");
		
		User found = users.findByEmailAndMethod("chico@brutal.com", MethodType.FACEBOOK);
		
		assertNotNull(found);
		assertEquals(user, found);
	}
	
	@Test
	public void should_verify_if_email_exists() throws Exception {
		saveUser("chico@brutal.com");
		assertTrue(users.existsWithEmail("chico@brutal.com"));
		assertFalse(users.existsWithEmail("whatever@dev.null"));
		assertFalse(users.existsWithEmail(null));
	}
	
	@Test
	public void should_verify_if_user_name_exists() throws Exception {
		String name = "Existent user";
		saveUser(name, "chico@brutal.com");
		assertTrue(users.existsWithName(name));
		assertTrue(users.existsWithName(name.toLowerCase()));
		assertFalse(users.existsWithEmail("unexistent name"));
		assertFalse(users.existsWithEmail(null));
	}

	private User saveUser(String email) {
		return saveUser("Chico Picadinho", email);
	}
	
	private User saveUser(String name, String email) {
		User user = user(name, email);
		LoginMethod facebookLogin = LoginMethod.facebookLogin(user, user.getEmail(), "1234");
		user.add(facebookLogin);
		session.save(user);
		session.save(facebookLogin);
		return user;
	}
	
	
}

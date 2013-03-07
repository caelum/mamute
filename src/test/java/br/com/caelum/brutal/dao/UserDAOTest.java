package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.vidageek.mirror.dsl.Mirror;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.infra.Digester;
import br.com.caelum.brutal.infra.MD5;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.User;

public class UserDAOTest extends DatabaseTestCase {

	private UserDAO users;

	@Before
	public void setup() {
		this.users = new UserDAO(session);
	}
	
	@Test
	public void should_search_by_email_and_password() {
		User guilherme = user("Guilherme Silveira", "guilherme@caelum.com.br");
		LoginMethod brutalLogin = LoginMethod.brutalLogin(guilherme, "guilherme@caelum.com.br", "654321");
		session.save(brutalLogin);
		users.save(guilherme);
		
		assertEquals(guilherme, users.findByMailAndPassword("guilherme@caelum.com.br", "654321"));
		assertNull(users.findByMailAndPassword("guilherme@caelum.com.br", "1234567"));
		assertNull(users.findByMailAndPassword("joao.silveira@caelum.com.br", "654321"));
	}
	
	@Test
	public void should_find_by_session_key() {
	    User guilherme = user("Guilherme Silveira", "guilherme@caelum.com.br");
	    users.save(guilherme);
	    guilherme.setSessionKey();
	    String sessionKey = guilherme.getSessionKey();
	    
	    assertEquals(guilherme, users.findBySessionKey(sessionKey));
	    assertNull(users.findBySessionKey("12345"));
	    assertNull(users.findBySessionKey(null));
	}
	
	@Test
	public void should_search_by_email_and_legacy_password_and_update_password() {
	    String password = "654321";
        User guilherme = user("Guilherme Silveira", "guilherme@caelum.com.br");
		LoginMethod brutalLogin = LoginMethod.brutalLogin(guilherme, "guilherme@caelum.com.br", password);
	    new Mirror().on(brutalLogin).set().field("token").withValue(MD5.crypt(password));

	    guilherme.add(brutalLogin);
	    session.save(brutalLogin);
	    users.save(guilherme);
	    
	    assertNull(users.findByMailAndPassword("guilherme@caelum.com.br", password));
	    
	    User found = users.findByMailAndLegacyPasswordAndUpdatePassword("guilherme@caelum.com.br", password);
        assertEquals(guilherme, found);
	    assertEquals(Digester.encrypt(password), brutalLogin.getToken());
	    
	    assertNull(users.findByMailAndPassword("joao.silveira@caelum.com.br", password));
	    assertNull(users.findByMailAndPassword("guilherme.silveira@caelum.com.br", "123456"));
	}
	
	
}

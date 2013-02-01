package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.User;
import br.com.caelum.pagpag.integracao.dao.DatabaseTestCase;

public class UserDAOTest extends DatabaseTestCase {

	private UserDAO users;

	@Before
	public void setup() {
		this.users = new UserDAO(session);
	}
	
	@Test
	public void should_search_by_email_and_password() {
		User guilherme = new User("Guilherme Silveira", "guilherme@caelum.com.br", "654321");
		users.save(guilherme);
		
		assertEquals(guilherme, users.findByMailAndPassword("guilherme@caelum.com.br", "654321"));
		assertNull(users.findByMailAndPassword("guilherme.silveira@caelum.com.br", "1234567"));
		assertNull(users.findByMailAndPassword("joao.silveira@caelum.com.br", "654321"));
	}
	
	
}

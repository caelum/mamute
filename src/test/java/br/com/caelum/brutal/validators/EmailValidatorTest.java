package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor4.util.test.MockValidator;

public class EmailValidatorTest {
	
	private EmailValidator emailValidator;
	private MockValidator validator;
	private MessageFactory messageFactory;
	private UserDAO users;
	private String existant;
	private String unexistant;

	@Before
	public void setup() {
		users = mock(UserDAO.class);
		messageFactory = mock(MessageFactory.class);
		validator = new MockValidator();
		emailValidator = new EmailValidator(validator, users, messageFactory);
		
		existant = "existant@brutal.com";
		unexistant = "unexistant@brutal.com";
		when(users.existsWithEmail(existant)).thenReturn(true);
		when(users.existsWithEmail(unexistant)).thenReturn(false);
		when(users.existsWithEmail(null)).thenReturn(false);
	}

	@Test
	public void existant_email_should_be_invalid() {
		assertFalse(emailValidator.validate(existant));
	}
	
	@Test
	public void unexistant_email_should_be_valid() {
		assertTrue(emailValidator.validate(unexistant));
	}
	
	@Test
	public void null_email_should_be_valid() {
		assertFalse(emailValidator.validate(null));
	}

}

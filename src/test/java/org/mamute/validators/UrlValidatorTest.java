package org.mamute.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mamute.validators.UrlValidator;

import br.com.caelum.vraptor.environment.Environment;

public class UrlValidatorTest {
	
	private Environment env;

	@Before
	public void setup() {
		env = mock(Environment.class);
		when(env.get("host")).thenReturn("http://localhost:8080/");
	}

	@Test
	public void should_check_url_outside_domain() throws Exception {
		UrlValidator urlValidator = new UrlValidator(env);
		assertFalse(urlValidator.isValid("http://mamutee.org/login"));
	}
	
	@Test
	public void should_check_url_inside_domain() throws Exception {
		UrlValidator urlValidator = new UrlValidator(env);
		assertTrue(urlValidator.isValid("http://localhost:8080/perguntas/lala"));
		assertTrue(urlValidator.isValid("http://localhost:8080/"));
		assertTrue(urlValidator.isValid("http://localhost:8080/facebook"));
	}



}

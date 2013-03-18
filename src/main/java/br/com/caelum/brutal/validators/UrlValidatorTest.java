package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.util.test.MockValidator;

public class UrlValidatorTest {
	
	private Validator validator;
	private Environment env;

	@Before
	public void setup() {
		validator = new MockValidator();
		env = mock(Environment.class);
		when(env.get("site.url")).thenReturn("http://localhost:8080/");
	}

	@Test
	public void should_check_url_outside_domain() throws Exception {
		UrlValidator urlValidator = new UrlValidator(env, validator);
		urlValidator.validate("http://gujj.com/login");
		assertTrue(validator.hasErrors());
	}
	
	@Test
	public void should_check_url_inside_domain() throws Exception {
		UrlValidator urlValidator = new UrlValidator(env, validator);
		urlValidator.validate("http://localhost:8080/perguntas/lala");
		urlValidator.validate("http://localhost:8080/");
		urlValidator.validate("http://localhost:8080/facebook");
		assertFalse(validator.hasErrors());
	}

}

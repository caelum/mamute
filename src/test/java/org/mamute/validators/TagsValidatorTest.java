package org.mamute.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import br.com.caelum.vraptor.environment.Environment;
import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.factory.MessageFactory;
import org.mamute.model.Tag;
import org.mamute.model.User;

import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.Validator;

public class TagsValidatorTest extends TestCase {

	private Environment environment;
	private MessageFactory messageFactory;
	private Validator validator;
	private TagsValidator tagsValidator;
	private Tag java;
	private Tag rails;
	private Tag ruby;
	private Tag nonalpha;
	private ResourceBundle bundle;

	@Before
	public void setup() {
		environment = mock(Environment.class);
		when(environment.get("tags.sanitizer.regex")).thenReturn("[a-zA-Z0-9-]");

		messageFactory = new MessageFactory(bundle);
		validator = new MockValidator();
		tagsValidator = new TagsValidator(environment, validator, messageFactory);
		User user = user("any", "any@brutal.com");
		java = new Tag("java", "", user);
		ruby = new Tag("ruby", "", user);
		rails = new Tag("rails", "", user);
		nonalpha = new Tag("java,mysql", "", user);
	}

	@Test
	public void should_not_validate_tags_not_found() throws Exception {
		List<String> wanted = Arrays.asList("java", "blabla", "blablablabla");
		List<Tag> found = Arrays.asList(java);
		assertFalse(tagsValidator.validate(found, wanted));
		assertEquals(2, validator.getErrors().size());
	}

	@Test
	public void should_validate_all_tags_found() throws Exception {
		List<String> wanted = Arrays.asList("java", "ruby", "rails");
		List<Tag> found = Arrays.asList(rails, java, ruby);
		assertTrue(tagsValidator.validate(found, wanted));
	}

	@Test
	public void should_prevent_creation_of_nonalpha_tag() {
		List<String> wanted = Arrays.asList("java,mysql", "ruby", "rails");
		List<Tag> found = Arrays.asList(nonalpha, ruby, rails);

		tagsValidator.validate(found, wanted);
		assertEquals(1, validator.getErrors().size());
	}

	@Test
	public void should_allow_creation_of_alpha_tag() {
		List<String> wanted = Arrays.asList("java", "ruby", "rails");
		List<Tag> found = Arrays.asList(rails, java, ruby);

		tagsValidator.validate(found, wanted);
		assertEquals(0, validator.getErrors().size());
	}

}

package br.com.caelum.brutal.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.util.test.MockLocalization;
import br.com.caelum.vraptor.util.test.MockValidator;

public class TagsValidatorTest {
	
	private MessageFactory messageFactory;
	private Localization localization;
	private Validator validator;
	
	@Before
	public void setup(){
		localization = new MockLocalization();
		messageFactory = new MessageFactory(localization);
		validator = new MockValidator();
	}

	@Test
	public void should_not_validate_null() {
		TagsValidator tagsValidator = new TagsValidator(validator, messageFactory);
		assertFalse(tagsValidator.validate(null));
	}
	
	@Test
	public void should_validate_not_null() {
		TagsValidator tagsValidator = new TagsValidator(validator, messageFactory);
		assertTrue(tagsValidator.validate(new Tag("java", "tag about java", null)));
	}
	
	@Test
	public void should_not_validate_empty_name() {
		TagsValidator tagsValidator = new TagsValidator(validator, messageFactory);
		assertFalse(tagsValidator.validate(new Tag("", "tag about java", null)));
	}

}

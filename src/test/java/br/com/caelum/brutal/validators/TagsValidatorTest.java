package br.com.caelum.brutal.validators;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.util.test.MockValidator;

public class TagsValidatorTest {

	@Test
	public void should_not_validate_null() {
		TagsValidator tagsValidator = new TagsValidator(new MockValidator());
		assertFalse(tagsValidator.validate(null));
	}
	
	@Test
	public void should_validate_not_null() {
		TagsValidator tagsValidator = new TagsValidator(new MockValidator());
		assertTrue(tagsValidator.validate(new Tag("java", "tag about java", null)));
	}
	
	@Test
	public void should_not_validate_empty_name() {
		TagsValidator tagsValidator = new TagsValidator(new MockValidator());
		assertFalse(tagsValidator.validate(new Tag("", "tag about java", null)));
	}

}

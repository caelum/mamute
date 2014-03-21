package org.mamute.validators;

import javax.inject.Inject;
import javax.validation.Valid;

import org.mamute.controllers.TagPageController;
import org.mamute.dao.TagPageDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.TagPage;

import br.com.caelum.vraptor.validator.Validator;

public class TagPageValidator {
	private Validator validator;
	private MessageFactory messageFactory;
	private TagPageDAO tagPages;

	@Deprecated
	public TagPageValidator() {
	}

	@Inject
	public TagPageValidator(Validator validator, MessageFactory messageFactory, TagPageDAO tagPages) {
		this.validator = validator;
		this.messageFactory = messageFactory;
		this.tagPages = tagPages;
	}
	
	public boolean validateCreationWithTag(String tagName){
		if(tagPages.existsOfTag(tagName)){
			validator.add(messageFactory.build("error", "tag_page.errors.already_exists", tagName));
		}
		validator.onErrorRedirectTo(TagPageController.class).showTagPage(tagName);
		return !validator.hasErrors();
	}

	public boolean validate(@Valid TagPage tagPage) {
		return !validator.hasErrors();
	}
	
	public <T> T onErrorRedirectTo(Class<T> controller) {
		return validator.onErrorRedirectTo(controller);
	}

}

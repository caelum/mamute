package org.mamute.validators;

import java.util.List;

import javax.inject.Inject;

import org.mamute.factory.MessageFactory;
import org.mamute.model.Tag;

import br.com.caelum.vraptor.validator.Validator;

public class TagsValidator {
	
	private final Validator validator;
	private final MessageFactory messageFactory;

	@Deprecated
	public TagsValidator() {
		this(null, null);
	}

	@Inject
	public TagsValidator(Validator validator, MessageFactory messageFactory) {
		this.validator = validator;
		this.messageFactory = messageFactory;
	}
	
	
	public boolean validate(List<Tag> found, List<String> wanted) {
		for (String name : wanted) {
			if (!isPresent(name, found)) {
				validator.add(messageFactory.build("error", "tag.errors.doesnt_exist", name));
			}
		}
		return !validator.hasErrors();
	}

	private boolean isPresent(String name, List<Tag> found) {
		for (Tag tag : found) {
			if (tag.getName().equals(name))
				return true;
		}
		return false;
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}

}

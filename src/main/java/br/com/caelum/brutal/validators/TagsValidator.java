package br.com.caelum.brutal.validators;

import java.util.List;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class TagsValidator {
	private final Validator validator;
	private MessageFactory messageFactory;

	public TagsValidator(Validator validator, MessageFactory messageFactory) {
		this.validator = validator;
		this.messageFactory = messageFactory;
	}
	
	
	public boolean validate(List<Tag> found, List<String> wanted) {
		validate(wanted);
		for (String name : wanted) {
			if (!isPresent(name, found)) {
				validator.add(messageFactory.build("error", "tag.errors.doesnt_exist", name));
			}
		}
		return !validator.hasErrors();
	}

	private boolean validate(List<String> tags) {
		if(tags.isEmpty()){
			validator.add(messageFactory.build("error", "question.errors.tags.empty"));
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

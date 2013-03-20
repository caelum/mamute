package br.com.caelum.brutal.validators;

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
	
	public boolean validate(Tag tag) {
		if (tag == null) {
			validator.add(messageFactory.build("error", "tag.errors.doesnt_exist"));
			return false;
		}
		if(tag.getName() == null || tag.getName().isEmpty()){
			validator.add(messageFactory.build("error", "tag.errors.empty_name"));
		}
		validator.validate(tag);
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}

}

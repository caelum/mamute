package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class TagsValidator {
	private final Validator validator;

	public TagsValidator(Validator validator) {
		this.validator = validator;
	}
	
	public boolean validate(Tag tag){
		if (tag == null) {
			validator.add(new ValidationMessage("tag.errors.doesnt_exist", "error"));
			return false;
		}
		if(tag.getName() == null || tag.getName().isEmpty()){
			validator.add(new ValidationMessage("tag.errors.empty_name", "error"));
		}
		validator.validate(tag);
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}

}

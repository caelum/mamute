package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
public class TagsValidator {
	private final Validator validator;

	public TagsValidator(Validator validator) {
		this.validator = validator;
	}
	
	public boolean validate(Tag tag){
		if (tag == null) {
			validator.add(new I18nMessage("error", "tag.errors.doesnt_exist"));
			return false;
		}
		if(tag.getName() == null || tag.getName().isEmpty()){
			validator.add(new I18nMessage("error", "tag.errors.empty_name"));
		}
		validator.validate(tag);
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}

}

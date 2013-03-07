package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestionInformationValidator {
	
	private final Validator validator;

	public QuestionInformationValidator(Validator validator) {
		this.validator = validator;
	}
	
	public boolean validate(QuestionInformation questionInformation){
		validator.validate(questionInformation);
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
	
}

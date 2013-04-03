package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AnsweredByValidator {
	private final Validator validator;
	private final LoggedUser user;
	private final MessageFactory factory;

	public AnsweredByValidator(Validator validator, LoggedUser user, MessageFactory factory) {
		this.validator = validator;
		this.user = user;
		this.factory = factory;
	}
	
	public boolean validate(Question question){
		if(question.answeredBy(user.getCurrent())){
			validator.add(factory.build("error", "answer.errors.already_answered"));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(Class<T> controller) {
		return validator.onErrorRedirectTo(controller);
	}
}

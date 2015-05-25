package org.mamute.validators;

import javax.inject.Inject;

import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;

import br.com.caelum.vraptor.validator.Validator;

public class AnsweredByValidator {
	private Validator validator;
	private LoggedUser user;
	private MessageFactory factory;
	private EnvironmentKarma environmentKarma;

	@Deprecated
	public AnsweredByValidator() {
	}
	
	@Inject
	public AnsweredByValidator(Validator validator, LoggedUser user, MessageFactory factory, EnvironmentKarma environmentKarma) {
		this.validator = validator;
		this.user = user;
		this.factory = factory;
		this.environmentKarma = environmentKarma;
	}
	
	public boolean validate(Question question){
		if(question.getAuthor().equals(user.getCurrent())) {
			if(!user.getCurrent().hasKarmaToAnswerOwnQuestion(environmentKarma)) {
				validator.add(factory.build("error", "answer.validation.errors.not_enough_karma"));	
			}
		} 
		if(question.alreadyAnsweredBy(user.getCurrent())){
			validator.add(factory.build("error", "answer.errors.already_answered"));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(Class<T> controller) {
		return validator.onErrorRedirectTo(controller);
	}
}

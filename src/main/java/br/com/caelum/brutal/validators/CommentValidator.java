package br.com.caelum.brutal.validators;

import br.com.caelum.brutal.model.Comment;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class CommentValidator {

	private Validator validator;

	public CommentValidator(Validator validator) {
		this.validator = validator;
	}
	
	public boolean validate(Comment comment){
		validator.validate(comment);
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}

}

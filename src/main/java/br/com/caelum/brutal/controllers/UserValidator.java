package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.ValidationMessage;


@Component
public class UserValidator{
	private final Validator validator;

	public UserValidator(Validator validator) {
		this.validator = validator;
	}
	
	public boolean validate(User user){
		if(user == null) validator.add(new I18nMessage("error","user.wrong"));
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
		
}

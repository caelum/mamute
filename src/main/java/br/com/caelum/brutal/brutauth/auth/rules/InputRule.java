package br.com.caelum.brutal.brutauth.auth.rules;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.handlers.InputHandler;
import br.com.caelum.brutal.input.InputManager;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@HandledBy(InputHandler.class)
public class InputRule implements CustomBrutauthRule{

	@Inject public LoggedUser user;
	@Inject public InputManager input;
	
	public boolean isAllowed(){
		User current = user.getCurrent();
		boolean isAllowed = input.can(current);
		if(isAllowed) input.ping(current);
		return isAllowed;
	}

}

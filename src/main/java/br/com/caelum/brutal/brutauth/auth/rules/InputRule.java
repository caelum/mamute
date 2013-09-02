package br.com.caelum.brutal.brutauth.auth.rules;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
	@Inject public HttpServletRequest request;
	
	public boolean isAllowed(){
		User current = user.getCurrent();
		boolean isAllowed = input.can(current);
		if(isAllowed && "POST".equals(request.getMethod())) input.ping(current);
		return isAllowed;
	}

}

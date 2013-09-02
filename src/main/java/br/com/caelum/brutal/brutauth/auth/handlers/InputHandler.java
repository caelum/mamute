package br.com.caelum.brutal.brutauth.auth.handlers;

import static br.com.caelum.vraptor4.view.Results.http;

import javax.inject.Inject;

import br.com.caelum.brutal.input.InputManager;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.vraptor4.Result;
import br.com.caelum.vraptor4.core.Localization;

public class InputHandler implements RuleHandler{

	@Inject public Result result;
	@Inject public InputManager input;
	@Inject public LoggedUser user;
	@Inject public Localization localization;
	
	@Override
	public void handle() {
		int remainingTime = input.getRemainingTime(user.getCurrent());
		String errorMessage = localization.getMessage("error.input_overflow", remainingTime);
		result.include("unauthorizedMessage", errorMessage).use(http()).sendError(403);
	}

}

package br.com.caelum.brutal.brutauth.auth.handlers;

import static java.util.Arrays.asList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.controllers.ListController;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.input.InputManager;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.vraptor4.Result;
import br.com.caelum.vraptor4.core.Localization;
import br.com.caelum.vraptor4.view.Results;

public class InputHandler implements RuleHandler{

	@Inject public Result result;
	@Inject public MessageFactory messages;
	@Inject public InputManager input;
	@Inject public LoggedUser user;
	@Inject public Localization localization;
	@Inject public HttpServletRequest req;
	
	@Override
	public void handle() {
		int remainingTime = input.getRemainingTime(user.getCurrent());
		if("application/json".equals(req.getHeader("Accept"))){
			result.use(Results.http()).body(localization.getMessage("error.input_overflow", remainingTime)).sendError(403);			
		}else{
			result.include("messages", asList(messages.build("error", "error.input_overflow", remainingTime)));
			result.redirectTo(ListController.class).home(1);
		}
	}

}

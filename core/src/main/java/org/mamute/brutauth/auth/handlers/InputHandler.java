package org.mamute.brutauth.auth.handlers;

import static java.text.MessageFormat.format;

import javax.inject.Inject;

import org.mamute.input.InputManager;
import org.mamute.model.LoggedUser;

import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.view.Results;

public class InputHandler implements RuleHandler{

	@Inject public Result result;
	@Inject public InputManager input;
	@Inject public LoggedUser user;
	@Inject public BundleFormatter bundle;
	
	@Override
	public void handle() {
		int remainingTime = input.getRemainingTime(user.getCurrent());
		String errorMessage = format(bundle.getMessage("error.input_overflow"), remainingTime);
		result.include("unauthorizedMessage", errorMessage).use(Results.http()).sendError(403);
	}

}

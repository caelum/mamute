package br.com.caelum.brutal.brutauth.auth.handlers;

import static java.util.Arrays.asList;

import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.controllers.AuthController;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

public class LoggedHandler implements RuleHandler {

	@Inject private Result result;
	@Inject private HttpServletRequest req;
	@Inject private MessageFactory messageFactory;
	@Inject private ResourceBundle bundle;

	@Override
	public void handle() {
		if("application/json".equals(req.getHeader("Accept"))){
			result.use(Results.http()).body(bundle.getString("error.requires_login")).sendError(403);			
		}else{
			result.include("messages", asList(messageFactory.build("alert", "auth.access.denied")));
			String redirectUrl = req.getRequestURL().toString();
			result.redirectTo(AuthController.class).loginForm(redirectUrl);
		}
	}

}

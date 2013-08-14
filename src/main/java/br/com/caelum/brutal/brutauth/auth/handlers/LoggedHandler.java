package br.com.caelum.brutal.brutauth.auth.handlers;

import static java.util.Arrays.asList;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.controllers.AuthController;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.vraptor4.core.Localization;

@Component
public class LoggedHandler implements RuleHandler{

	private final Result result;
	private final HttpServletRequest req;
	private final MessageFactory messageFactory;
	private final Localization localization;

	public LoggedHandler(Result result, HttpServletRequest req, MessageFactory messageFactory, Localization localization) {
		this.result = result;
		this.req = req;
		this.messageFactory = messageFactory;
		this.localization = localization;
	}
	
	@Override
	public void handle() {
		if("application/json".equals(req.getHeader("Accept"))){
			result.use(Results.http()).body(localization.getMessage("error.requires_login")).sendError(403);			
		}else{
			result.include("messages", asList(messageFactory.build("alert", "auth.access.denied")));
			String redirectUrl = req.getRequestURL().toString();
			result.redirectTo(AuthController.class).loginForm(redirectUrl);
		}
	}

}

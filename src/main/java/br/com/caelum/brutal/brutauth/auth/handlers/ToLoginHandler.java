package br.com.caelum.brutal.brutauth.auth.handlers;

import static java.util.Arrays.asList;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.controllers.AuthController;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ToLoginHandler implements RuleHandler{

	private final Result result;
	private final HttpServletRequest req;
	private final MessageFactory messageFactory;

	public ToLoginHandler(Result result, HttpServletRequest req, MessageFactory messageFactory) {
		this.result = result;
		this.req = req;
		this.messageFactory = messageFactory;
	}
	
	@Override
	public boolean handle(boolean isAllowed) {
		result.include("messages", asList(messageFactory.build("alert", "auth.access.denied")));
        String redirectUrl = req.getRequestURL().toString();
        result.redirectTo(AuthController.class).loginForm(redirectUrl);
        return isAllowed;
	}

}

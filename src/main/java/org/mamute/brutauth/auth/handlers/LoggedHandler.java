package org.mamute.brutauth.auth.handlers;

import static java.util.Arrays.asList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.mamute.controllers.AuthController;
import org.mamute.factory.MessageFactory;

import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.view.Results;

public class LoggedHandler implements RuleHandler {

	@Inject private Result result;
	@Inject private HttpServletRequest req;
	@Inject private MessageFactory messageFactory;
	@Inject private BundleFormatter bundle;

	@Override
	public void handle() {
		if("application/json".equals(req.getHeader("Accept"))){
			result.use(Results.http()).body(bundle.getMessage("error.requires_login")).sendError(403);			
		}else{
			result.include("loginRequiredMessages", asList(messageFactory.build("alert", "auth.access.denied")));
			String redirectUrl = req.getRequestURL().toString();
			result.redirectTo(AuthController.class).loginForm(redirectUrl);
		}
	}

}

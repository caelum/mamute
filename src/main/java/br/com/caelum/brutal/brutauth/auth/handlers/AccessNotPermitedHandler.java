package br.com.caelum.brutal.brutauth.auth.handlers;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AccessNotPermitedHandler implements RuleHandler{
	private final Result result;

	public AccessNotPermitedHandler(Result result) {
		this.result = result;
	}
	
	@Override
	public boolean handle(boolean isAllowed) {
		if(!isAllowed) result.use(http()).sendError(403);
		return isAllowed;
	}

}

package br.com.caelum.brutal.auth;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;

@Intercepts(before = LoggedUserInterceptor.class)
public class AjaxAuthInterceptor implements Interceptor {

	private final String acceptHeader;
	private final Result result;
	private final LoggedUser loggedUser;
	private final Localization localization;

	public AjaxAuthInterceptor(HttpServletRequest req, Result result, LoggedUser loggedUser, Localization localization) {
		this.result = result;
		this.loggedUser = loggedUser;
		this.localization = localization;
		acceptHeader = req.getHeader("Accept");
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		boolean acceptsJson = "application/json".equals(acceptHeader);
		return method.containsAnnotation(LoggedAccess.class) && acceptsJson;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object obj) throws InterceptionException {
		if (!loggedUser.isLoggedIn()) {
			result.use(Results.http()).body(localization.getMessage("error.requires_login")).sendError(403);
			return;
		}
		stack.next(method, obj);
	}

}

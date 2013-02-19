package br.com.caelum.brutal.providers;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.ocpsoft.prettytime.PrettyTime;

import br.com.caelum.brutal.auth.Access;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Component
@Intercepts(before=ParametersInstantiatorInterceptor.class)
public class GlobalInterceptor implements Interceptor{
	
	private final Environment env;
	private final Result result;
	private final Access access;
	private final HttpServletRequest req;
	private final Localization localization;

	public GlobalInterceptor(Environment env, Result result, Access access, HttpServletRequest req, Localization localization) {
		this.env = env;
		this.result = result;
		this.access = access;
		this.req = req;
		this.localization = localization;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		result.include("env", env);
		result.include("currentUser", access.getInstance());
		result.include("prettyTimeFormatter", new PrettyTime(localization.getLocale()));
		result.include("literalFormatter", DateTimeFormat.forPattern(localization.getMessage("date.joda.pattern")).withLocale(localization.getLocale()));
		result.include("currentUrl", req.getRequestURL());
		stack.next(method, resourceInstance);
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}

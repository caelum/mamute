package br.com.caelum.brutal.providers;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
	private static final Logger LOG = Logger.getLogger(GlobalInterceptor.class);

	public GlobalInterceptor(Environment env, Result result, Access access, 
			HttpServletRequest req, Localization localization, ServletContext servletContext) {
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
		result.include("contextPath", req.getContextPath());

		logHeaders();
		
		stack.next(method, resourceInstance);
	}

	private void logHeaders() {
		Enumeration<String> headerNames = req.getHeaderNames();
		LOG.debug("headers received from request");
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			Enumeration<String> values = req.getHeaders(key);
			LOG.debug(key);
			LOG.debug(values);
		}
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}

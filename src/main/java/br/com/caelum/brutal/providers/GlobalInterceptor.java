package br.com.caelum.brutal.providers;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.ocpsoft.prettytime.PrettyTime;

import br.com.caelum.brutal.infra.MenuInfo;
import br.com.caelum.brutal.infra.NotFoundException;
import br.com.caelum.brutal.model.LoggedUser;
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
public class GlobalInterceptor implements Interceptor {
	
	private final Environment env;
	private final Result result;
	private final HttpServletRequest req;
	private final Localization localization;
	private static final Logger LOG = Logger.getLogger(GlobalInterceptor.class);
	private final MenuInfo menuInfo;

	public GlobalInterceptor(Environment env, Result result, 
			HttpServletRequest req, Localization localization,  
			ServletContext servletContext, LoggedUser loggedUser,
			MenuInfo menuInfo) {
		this.env = env;
		this.result = result;
		this.req = req;
		this.localization = localization;
		this.menuInfo = menuInfo;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		menuInfo.include();
		result.include("env", env);
		result.include("prettyTimeFormatter", new PrettyTime(localization.getLocale()));
		result.include("literalFormatter", DateTimeFormat.forPattern(localization.getMessage("date.joda.pattern")).withLocale(localization.getLocale()));
		result.include("currentUrl", getCurrentUrl());
		result.include("contextPath", req.getContextPath());
		result.include("deployTimestamp", deployTimestamp());
		result.on(NotFoundException.class).notFound();
		
		logHeaders();
		
		stack.next(method, resourceInstance);
	}

	private String deployTimestamp() {
		return System.getProperty("deploy.timestamp", "");
	}

	private String getCurrentUrl() {
		String host = req.getHeader("Host");
		String url;
		if (host == null) {
			url = req.getRequestURL().toString();
		} else {
			url = "http://" + host + req.getRequestURI(); 
		}
		LOG.debug("setting url: " + url);
		return url;
	}

	private void logHeaders() {
		Enumeration<String> headerNames = req.getHeaderNames();
		LOG.debug("headers received from request");
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = req.getHeader(key);
			LOG.debug(key);
			LOG.debug(value);
		}
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}

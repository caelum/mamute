package org.mamute.providers;

import static java.util.Arrays.asList;

import java.util.Enumeration;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.mamute.ads.BrutalAds;
import org.mamute.auth.BannedUserException;
import org.mamute.controllers.AuthController;
import org.mamute.factory.MessageFactory;
import org.mamute.infra.MenuInfo;
import org.mamute.infra.NotFoundException;
import org.mamute.infra.SideBarInfo;
import org.mamute.util.BrutalDateFormat;
import org.ocpsoft.prettytime.PrettyTime;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.interceptor.FlashInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
@Intercepts (after=FlashInterceptor.class)
public class GlobalInterceptor implements Interceptor{
	
	private static final String SLASH_AT_END = "/$";
	private static final Logger LOG = Logger.getLogger(GlobalInterceptor.class);	
	@Inject private Environment env;
	@Inject private Result result;
	@Inject private HttpServletRequest req;
	@Inject private Locale locale;
	@Inject private MenuInfo menuInfo;
	@Inject private BrutalDateFormat brutalDateFormat;
	@Inject private MessageFactory messageFactory;
	@Inject private BrutalAds ads;
	@Inject private SideBarInfo sideBarInfo;

	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object resourceInstance) throws InterceptionException {
		menuInfo.include();
		sideBarInfo.include();
		
		result.include("env", env);
		result.include("prettyTimeFormatter", new PrettyTime(locale));
		result.include("literalFormatter", brutalDateFormat.getInstance("date.joda.pattern"));
		result.include("currentUrl", getCurrentUrl());
		result.include("contextPath", req.getContextPath());
		result.include("deployTimestamp", deployTimestamp());
		result.include("shouldShowAds", ads.shouldShowAds());
		result.on(NotFoundException.class).notFound();
		result.on(BannedUserException.class)
				.include("errors", asList(messageFactory.build("error", "user.errors.banned")))
				.redirectTo(AuthController.class).loginForm("");
		
		LOG.debug("request for: " + req.getRequestURI());
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
		if(url.endsWith("/")) url = url.split(SLASH_AT_END)[0];
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

	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}

}

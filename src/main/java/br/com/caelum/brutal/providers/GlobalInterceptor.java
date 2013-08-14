package br.com.caelum.brutal.providers;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.ocpsoft.prettytime.PrettyTime;

import br.com.caelum.brutal.ads.BrutalAds;
import br.com.caelum.brutal.auth.BannedUserException;
import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.controllers.AuthController;
import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.infra.MenuInfo;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.util.BrutalDateFormat;
import br.com.caelum.brutauth.interceptors.CustomBrutauthRuleInterceptor;
import br.com.caelum.brutauth.interceptors.SimpleBrutauthRuleInterceptor;
import br.com.caelum.vraptor.plugin.hibernate4.extra.ParameterLoaderInterceptor;
import br.com.caelum.vraptor4.BeforeCall;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.core.Localization;


@Intercepts(before={ParameterLoaderInterceptor.class, CustomBrutauthRuleInterceptor.class, SimpleBrutauthRuleInterceptor.class})
public class GlobalInterceptor {
	
	private static final String SLASH_AT_END = "/$";
	private final Environment env;
	private final Result result;
	private final HttpServletRequest req;
	private final Localization localization;
	private static final Logger LOG = Logger.getLogger(GlobalInterceptor.class);
	private final MenuInfo menuInfo;
	private NewsDAO newses;
	private RecentTagsContainer recentTagsContainer;
	private BrutalDateFormat brutalDateFormat;
	private final MessageFactory messageFactory;
	private final BrutalAds ads;

	@Deprecated
	public GlobalInterceptor() {}

	@Inject
	public GlobalInterceptor(Environment env, Result result, 
			HttpServletRequest req, Localization localization,  
			ServletContext servletContext, LoggedUser loggedUser,
			MenuInfo menuInfo, NewsDAO newses,
			RecentTagsContainer recentTagsContainer,
			BrutalDateFormat brutalDateFormat, MessageFactory messageFactory,
			BrutalAds ads) {
		this.env = env;
		this.result = result;
		this.req = req;
		this.localization = localization;
		this.menuInfo = menuInfo;
		this.newses = newses;
		this.recentTagsContainer = recentTagsContainer;
		this.brutalDateFormat = brutalDateFormat;
		this.messageFactory = messageFactory;
		this.ads = ads;
	}

	@BeforeCall
	public void intercept() {
		menuInfo.include();
		result.include("env", env);
		result.include("prettyTimeFormatter", new PrettyTime(localization.getLocale()));
		result.include("literalFormatter", brutalDateFormat.getInstance("date.joda.pattern"));
		result.include("currentUrl", getCurrentUrl());
		result.include("contextPath", req.getContextPath());
		result.include("deployTimestamp", deployTimestamp());
		result.include("sidebarNews", newses.allVisibleAndApproved(5));
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("shouldShowAds", ads.shouldShowAds());
		result.on(NotFoundException.class).notFound();
		result.on(BannedUserException.class)
				.include("errors", asList(messageFactory.build("error", "user.errors.banned")))
				.redirectTo(AuthController.class).loginForm("");
		
		logHeaders();
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

}

package org.mamute.auth;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.mamute.qualifiers.Facebook;
import org.mamute.qualifiers.Google;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.Google2Api;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.environment.Environment;
public class OAuthServiceCreator {
	
	public static final String FACEBOOK_APP_SECRET = "facebook.app_secret";
	public static final String FACEBOOK_REDIRECT_URI = "facebook.redirect_uri";
	public static final String FACEBOOK_CLIENT_ID = "facebook.app_id";

	private static final String GOOGLE_CLIENT_ID = "google.client_id";
	private static final String GOOGLE_CLIENT_SECRET = "google.client_secret";
	private static final String GOOGLE_REDIRECT_URI = "google.redirect_uri";
	
	private OAuthService service;
	
	private Environment env;
	
	@Deprecated
	public OAuthServiceCreator(){}

	@Inject
	public OAuthServiceCreator(Environment env) {
		this.env = env;
	}
	
	@PostConstruct
	public void create() {
	}

	@Produces
	@Facebook
	public OAuthService getInstanceFacebook() {
		this.service = new ServiceBuilder()
		.provider(FacebookApi.class)
		.apiKey(env.get(FACEBOOK_CLIENT_ID))
		.apiSecret(env.get(FACEBOOK_APP_SECRET))
				.callback(env.get("host")+env.get(FACEBOOK_REDIRECT_URI))
		.build();
		return service;
	}
	
	@Produces
	@Google
	public OAuthService getInstanceGoogle() {
		this.service = new ServiceBuilder()
		.provider(Google2Api.class)
		.apiKey(env.get(GOOGLE_CLIENT_ID))
		.apiSecret(env.get(GOOGLE_CLIENT_SECRET))
		.callback(env.get("host")+env.get(GOOGLE_REDIRECT_URI))
		.scope("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
		.build();
		return service;
	}
}

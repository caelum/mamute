package br.com.caelum.brutal.auth;

import javax.annotation.PostConstruct;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class OAuthServiceCreator implements ComponentFactory<OAuthService> {
	
	public static final String FACEBOOK_APP_SECRET = "facebook.app_secret";
	public static final String FACEBOOK_REDIRECT_URI = "facebook.redirect_uri";
	public static final String FACEBOOK_CLIENT_ID = "facebook.client_id";
	
	private OAuthService service;
	private String appSecret;
	private String redirectUri;
	private String clientId;

	public OAuthServiceCreator(Environment env) {
		this.clientId = env.get(FACEBOOK_CLIENT_ID);
		this.redirectUri = env.get(FACEBOOK_REDIRECT_URI);
		this.appSecret = env.get(FACEBOOK_APP_SECRET);
	}
	
	@PostConstruct
	public void create() {
		this.service = new ServiceBuilder()
			.provider(FacebookApi.class)
			.apiKey(clientId)
			.apiSecret(appSecret)
			.callback(redirectUri)
			.build();
	}

	@Override
	public OAuthService getInstance() {
		return service;
	}

}

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
	
	private OAuthService service;
	private String appSecret;
	private String redirectUri;
	private String clientId;

	public OAuthServiceCreator(Environment env) {
		this.clientId = env.get("facebook.client_id");
		this.redirectUri = env.get("facebook.redirect_uri");
		this.appSecret = env.get("facebook.app_secret");
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

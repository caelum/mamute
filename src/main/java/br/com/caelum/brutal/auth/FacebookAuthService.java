package br.com.caelum.brutal.auth;

import java.util.Random;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FacebookAuthService {
	
	private static final Token EMPTY_TOKEN = null;
	private final String redirectUri;
	private final String clientId;
	private final String appSecret;
	private OAuthService service;

	public FacebookAuthService(Environment env) {
		this.clientId = env.get("facebook.client_id");
		this.redirectUri = env.get("facebook.redirect_uri");
		this.appSecret = env.get("facebook.app_secret");
		this.service = new ServiceBuilder()
			.provider(FacebookApi.class)
			.apiKey(clientId)
			.apiSecret(appSecret)
			.callback(redirectUri)
			.build();
	}
	
	public String getOauthUrl() {
		String facebookUrl = "https://www.facebook.com/dialog/oauth?" +
				"client_id=" + clientId + "&" +
				"redirect_uri=" + redirectUri + "&" +
				"state=" + new Random().nextLong();
		
		return facebookUrl;
	}

	public void buildToken(String code) {
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, new Verifier(code));
		System.out.println("gotcha! " + accessToken);
		throw new UnsupportedOperationException("do something with it: " + accessToken);
	}
	
}

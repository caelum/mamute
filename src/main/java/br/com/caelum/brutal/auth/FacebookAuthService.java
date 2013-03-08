package br.com.caelum.brutal.auth;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class FacebookAuthService {
	
	private static final Token EMPTY_TOKEN = null;
	private final OAuthService service;
	private Token accessToken;

	public FacebookAuthService(OAuthService service) {
		this.service = service;
	}
	
	public String getOauthUrl() {
		return service.getAuthorizationUrl(EMPTY_TOKEN);
	}

	public void buildToken(String code) {
		this.accessToken = service.getAccessToken(EMPTY_TOKEN, new Verifier(code));
	}
	
}

package br.com.caelum.brutal.auth;

import java.util.Random;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FacebookAuthService {
	
	private String redirectUri;
	private String clientId;

	public FacebookAuthService(Environment env) {
		clientId = env.get("facebook.client_id");
		redirectUri = env.get("facebook.redirect_uri");
	}
	
	public String getOauthUrl() {
		String facebookUrl = "https://www.facebook.com/dialog/oauth?" +
				"client_id=" + clientId + "&" +
				"redirect_uri=" + redirectUri + "&" +
				"state=" + new Random().nextLong();
		
		return facebookUrl;
	}

	public void buildToken(String code) {
		System.out.println(code);
	}
	
}

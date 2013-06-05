package br.com.caelum.brutal.auth;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.ioc.Component;

import com.google.gson.JsonObject;

@Component
public class FacebookAuthService {
	
	private static final Token EMPTY_TOKEN = null;
	private final OAuthService service;
	private Token accessToken;

	public FacebookAuthService(OAuthService service) {
		this.service = service;
	}
	
	public String getOauthUrl(String state) {
		String url = service.getAuthorizationUrl(EMPTY_TOKEN) + "&scope=email,user_location";
		if (state == null) {
			return url;
		}
		return url + "&state=" + state;
	}

	public String buildToken(String code) {
		this.accessToken = service.getAccessToken(EMPTY_TOKEN, new Verifier(code));
		return accessToken.getToken();
	}
	
	public SignupInfo getSignupInfo() {
		FacebookApi facebookApi = new FacebookApi(service, accessToken);
		JsonObject response = facebookApi.getSignupInfo();
		
		return SignupInfo.fromFacebook(response);
	}
	
}

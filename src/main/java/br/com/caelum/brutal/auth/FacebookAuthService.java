package br.com.caelum.brutal.auth;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
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
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me?fields=name,email,location");
		service.signRequest(accessToken, request);
		Response response = request.send();
		
		return SignupInfo.fromFacebook(response.getBody());
	}
	
}

package org.mamute.auth;

import javax.inject.Inject;

import org.mamute.qualifiers.Facebook;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.gson.JsonObject;

public class FacebookAuthService {
	
	private static final Token EMPTY_TOKEN = null;
	
	@Inject @Facebook private OAuthService service;
	private Token accessToken;

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
		FacebookAPI facebookApi = new FacebookAPI(service, accessToken);
		JsonObject response = facebookApi.getSignupInfo();
		
		return SignupInfo.fromFacebook(response);
	}
	
}

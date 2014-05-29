package org.mamute.auth;

import javax.inject.Inject;

import org.mamute.qualifiers.Facebook;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class FacebookAuthService {
	private static final Token EMPTY_TOKEN = null;
	@Inject @Facebook private OAuthService service;
	
	public String getOauthUrl(String state) {
		String url = service.getAuthorizationUrl(EMPTY_TOKEN) + "&scope=email,user_location";
		if (state == null) {
			return url;
		}
		return url + "&state=" + state;
	}
}

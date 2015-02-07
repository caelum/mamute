package org.mamute.auth;

import javax.inject.Inject;

import org.mamute.qualifiers.Google;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class GoogleAuthService {
	private static final Token EMPTY_TOKEN = null;
	@Inject @Google private OAuthService service;
	
	public String getOauthUrl(String redirect) {
		String url = service.getAuthorizationUrl(EMPTY_TOKEN);
		if (redirect == null) {
			return url;
		}
		return url + "&state=" + redirect;
	}
}

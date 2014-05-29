package org.mamute.auth;

import org.scribe.model.Token;

public interface SocialAPI {
	public SignupInfo getSignupInfo();
	public Token getAccessToken();
}

package org.mamute.auth;

import org.scribe.model.Token;

import com.google.common.base.Optional;

public interface SocialAPI {
	public Optional<SignupInfo> getSignupInfo();
	public Token getAccessToken();
}

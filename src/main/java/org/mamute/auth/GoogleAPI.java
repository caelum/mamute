package org.mamute.auth;

import static org.mamute.model.SanitizedText.fromTrustedText;

import org.mamute.model.MethodType;
import org.mamute.model.SanitizedText;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.common.base.Optional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleAPI implements SocialAPI{
	private OAuthService service;
	private Token accessToken;
	
	public GoogleAPI(Token accessToken, OAuthService service) {
		this.accessToken = accessToken;
		this.service = service;
	}
	
	public Optional<SignupInfo> getSignupInfo() {
		JsonObject jsonObject = new JsonParser().parse(makeRequest(getAccessToken()).getBody()).getAsJsonObject();
	    String email = jsonObject.get("emails").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
	    String name = jsonObject.get("displayName").getAsString();
	    String photoUrl = jsonObject.get("image").getAsJsonObject().get("url").getAsString();
	    
	    SignupInfo signupInfo = new SignupInfo(MethodType.GOOGLE, email, fromTrustedText(name), "", photoUrl);
	    return Optional.of(signupInfo);
	}
	
	private Response makeRequest(Token accessToken) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/me");
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		return request.send();
	}

	public Token getAccessToken() {
		return accessToken;
	}
}

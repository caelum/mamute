package org.mamute.auth;

import javax.inject.Inject;

import org.mamute.model.MethodType;
import org.mamute.qualifiers.Google;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleAPI {
	@Inject @Google private OAuthService service;
	private Response response;

	public Token getAccessToken(String code) {
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(null, verifier);
		sendRequest(accessToken);
		return accessToken;
	}

	public void sendRequest(Token accessToken) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/me");
		service.signRequest(accessToken, request);
	    request.addHeader("GData-Version", "3.0");
	    response = request.send();
	}
	
	public SignupInfo getSignupInfo() {
		JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
	    String email = jsonObject.get("emails").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
	    String name = jsonObject.get("displayName").getAsString();
	    String photoUrl = jsonObject.get("image").getAsJsonObject().get("url").getAsString();
	    
	    SignupInfo signupInfo = new SignupInfo(MethodType.GOOGLE, email, name, "", photoUrl);
	    return signupInfo;
	}
}

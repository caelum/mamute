package br.com.caelum.brutal.auth;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FacebookAPI {

	private final Token accessToken;
	private final OAuthService service;

	public FacebookAPI(OAuthService service, Token accessToken) {
		this.service = service;
		this.accessToken = accessToken;
	}

	public JsonObject getSignupInfo() {
		String url = "https://graph.facebook.com/me?fields=name,email,location,username";
		Response response = makeRequest(url);
		return new JsonParser().parse(response.getBody()).getAsJsonObject();
	}

	public String getUserName() {
		String url = "https://graph.facebook.com/me?fields=username";
		Response response = makeRequest(url);
		String body = response.getBody();
		if (response.getCode() != 200) {
			throw new IllegalArgumentException("http error: " + response.getCode() + ", facebook response body: " + body);
		}
		JsonObject jsonObj = new JsonParser().parse(body).getAsJsonObject();
		JsonElement jsonElement = jsonObj.get("username");
		if (jsonElement == null) {
			throw new IllegalArgumentException("facebook did not sent username! response body: " + body);
		}
		System.out.println(body);
		return jsonElement.getAsString();
	}

	private Response makeRequest(String url) {
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		service.signRequest(accessToken, request);
		Response response = request.send();
		return response;
	}
}

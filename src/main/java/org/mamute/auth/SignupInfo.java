package org.mamute.auth;

import static org.mamute.model.SanitizedText.fromTrustedText;

import java.net.MalformedURLException;
import java.net.URL;

import org.mamute.model.MethodType;
import org.mamute.model.SanitizedText;

import com.google.common.base.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SignupInfo {

	private final MethodType method;
	private final String email;
	private final SanitizedText name;
	private final String location;
	private String photoUrl;

	public SignupInfo(MethodType method, String email, SanitizedText name,
			String location, String photoUrl) {
		this.method = method;
		this.email = email;
		this.name = name;
		this.location = location;
		this.photoUrl = photoUrl;
	}

	public static Optional<SignupInfo> fromFacebook(JsonObject jsonObj) {
		JsonElement emailElement = jsonObj.get("email");
		if (emailElement == null) {
			return Optional.absent();
		}
		String email = emailElement.getAsString();
		String name = jsonObj.get("name").getAsString();
		JsonElement userIdObj = jsonObj.get("id");
		String userId = userIdObj != null ? userIdObj.getAsString() : null;
		String photoUrl = userId != null ? "http://graph.facebook.com/"+userId+"/picture" : null;
		JsonObject locationJson = jsonObj.getAsJsonObject("location");
		String location = "";
		if (locationJson != null) {
			location = locationJson.get("name").getAsString();
		}
		return Optional.of(new SignupInfo(MethodType.FACEBOOK, email, fromTrustedText(name), location, photoUrl));
	}

	public MethodType getMethod() {
		return method;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name.getText();
	}

	public String getLocation() {
		return location;
	}
	
	public URL getPhotoUri() {
		return getUrl(photoUrl);
	}
	
	public boolean containsPhotoUrl() {
		return photoUrl != null;
	}

	private URL getUrl(String photoUri) {
		if(photoUri == null) return null;
		try {
			return new URL(photoUri);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}

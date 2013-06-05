package br.com.caelum.brutal.auth;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.caelum.brutal.model.MethodType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class SignupInfo {

	private final MethodType method;
	private final String email;
	private final String name;
	private final String location;
	private String username;

	public SignupInfo(MethodType method, String email, String name,
			String location, String username) {
		this.method = method;
		this.email = email;
		this.name = name;
		this.location = location;
		this.username = username;
	}

	public static SignupInfo fromFacebook(JsonObject jsonObj) {
		JsonElement emailElement = jsonObj.get("email");
		if (emailElement == null) {
			throw new IllegalArgumentException("could not find email in json facebook response");
		}
		String email = emailElement.getAsString();
		String name = jsonObj.get("name").getAsString();
		String username = jsonObj.get("username").getAsString();
		JsonObject locationJson = jsonObj.getAsJsonObject("location");
		String location = "";
		if (locationJson != null) {
			location = locationJson.get("name").getAsString();
		}
		return new SignupInfo(MethodType.FACEBOOK, email, name, location, username);
	}

	public MethodType getMethod() {
		return method;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}
	
	public URL getFacebookPhotoUri() {
		String photoUri = "http://graph.facebook.com/"+username+"/picture";
		return getUrl(photoUri);
	}

	private URL getUrl(String photoUri) {
		try {
			return new URL(photoUri);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}

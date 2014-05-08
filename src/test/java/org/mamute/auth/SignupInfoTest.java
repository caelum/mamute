package org.mamute.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.mamute.model.MethodType;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SignupInfoTest {

	@Test
	public void should_build_info_from_facebook_json() {
		InputStream is = getClass().getResourceAsStream("/facebook-user.json");
		String json = new Scanner(is).useDelimiter("$$").next();
		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		
		SignupInfo info = SignupInfo.fromFacebook(jsonObject);
		assertEquals("chico@brutal.com", info.getEmail());
		assertEquals("Francisco Sokol", info.getName());
		assertEquals("São Paulo, Brazil", info.getLocation());
		assertTrue(info.getFacebookPhotoUri().getPath().contains("100001959511194"));
		assertEquals(MethodType.FACEBOOK, info.getMethod());
	}

}

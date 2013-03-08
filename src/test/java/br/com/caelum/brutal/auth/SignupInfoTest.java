package br.com.caelum.brutal.auth;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import br.com.caelum.brutal.model.MethodType;

public class SignupInfoTest {

	@Test
	public void should_build_info_from_facebook_json() {
		InputStream is = getClass().getResourceAsStream("/facebook-user.json");
		String json = new Scanner(is).useDelimiter("$$").next();
		
		SignupInfo info = SignupInfo.fromFacebook(json);
		assertEquals("chico@brutal.com", info.getEmail());
		assertEquals("Francisco Sokol", info.getName());
		assertEquals("São Paulo, Brazil", info.getLocation());
		assertEquals(MethodType.FACEBOOK, info.getMethod());
	}

}

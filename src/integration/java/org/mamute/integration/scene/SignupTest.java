package org.mamute.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.Test;

public class SignupTest extends AcceptanceTestBase {

	@After
	public void logout() {
		if (home().isLoggedIn())
			home().logOut();
	}

	@Test
	public void should_sign_up_through_login_page() {
		boolean loggedIn = home().toLoginPage()
				.signUp("leonardo", randomEmail(), "123456")
				.isLoggedIn();
		assertTrue(loggedIn);
	}

	private String randomEmail() {
		return "leo" + new Random().nextLong() + "@leo.com.br";
	}

}
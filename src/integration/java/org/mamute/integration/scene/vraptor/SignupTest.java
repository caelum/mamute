package org.mamute.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class SignupTest extends CustomVRaptorIntegration {

	Random randomizer = new Random();

	@Test
	public void should_sign_up() {
		String email = randomEmail();
		UserFlow navigation = signUp(navigate(),
				"Random User", email, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
		navigation = login(navigation, email);
		VRaptorTestResult signedUpUser = navigation.followRedirect().execute();
		signedUpUser.wasStatus(200).isValid();

		List<String> messagesList = messagesList(signedUpUser);
		assertTrue(messagesList.contains(message("signup.confirmation")));
	}

	@Test
	public void should_not_sign_up_with_invalid_email() {
		UserFlow navigation = signUp(navigate(),
				"Marco", "invalidEmail", DEFAULT_PASSWORD, DEFAULT_PASSWORD);
		VRaptorTestResult notSignedUpUser = navigation.followRedirect().execute();
		notSignedUpUser.wasStatus(200).isValid();

		List<String> errorsList = errorsList(notSignedUpUser);
		assertTrue(errorsList.contains(message("user.errors.email.invalid")));
	}

	@Test
	public void should_not_sign_up_with_an_existent_email() {
		UserFlow navigation = signUp(navigate(),
				"Marco", "invalidEmail", DEFAULT_PASSWORD, DEFAULT_PASSWORD);
		VRaptorTestResult notSignedUpUser = navigation.followRedirect().execute();
		notSignedUpUser.wasStatus(200).isValid();

		List<String> errorsList = errorsList(notSignedUpUser);
		assertTrue(errorsList.contains(message("user.errors.email.invalid")));
	}

	@Test
	public void should_not_sign_up_with_different_passwords() {
		UserFlow navigation = signUp(navigate(),
				"Marco", randomEmail(), DEFAULT_PASSWORD, "misspelledPassword");
		VRaptorTestResult notSignedUpUser = navigation.followRedirect().execute();
		notSignedUpUser.wasStatus(200).isValid();

		List<String> errorsList = errorsList(notSignedUpUser);
		assertTrue(errorsList.contains(message("signup.errors.password_confirmation")));
	}

	@Test
	public void should_not_sign_up_with_a_short_password() {
		UserFlow navigation = signUp(navigate(),
				"Marco", randomEmail(), "123", "123");
		VRaptorTestResult notSignedUpUser = navigation.followRedirect().execute();
		notSignedUpUser.wasStatus(200).isValid();

		List<String> errorsList = errorsList(notSignedUpUser);
		assertTrue(errorsList.contains(message("signup.errors.password.length")));
	}

	protected UserFlow signUp(UserFlow navigation, String name, String email, String password, String passwordConfirmation) {
		return navigation.post("/cadastrar",
				initWith("name", name)
					.add("email", email)
					.add("password", password)
					.add("passwordConfirmation", passwordConfirmation));
	}

	public String randomEmail () {
		return String.format("random%s@random.com", randomizer.nextLong());
	}

}

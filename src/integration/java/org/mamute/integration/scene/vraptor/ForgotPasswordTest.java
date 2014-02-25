package org.mamute.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mamute.integration.util.DaoManager;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class ForgotPasswordTest extends CustomVRaptorIntegration {

	private static DaoManager manager;
	private User user;
	String password;

	@Before
	public void setup() throws IOException {
		manager = new DaoManager();
		user = manager.randomUser();
		password = "newpass";
	}

	@Test
	public void should_deny_recovery_for_inexistant_email() {
		UserFlow navigation = tryToSendResetPasswordEmail("unexistant@brutal.com");
		VRaptorTestResult sentEmail = navigation.followRedirect().execute();
		sentEmail.wasStatus(200).isValid();
		List<String> errorsList = errorsList(sentEmail);
		assertTrue(errorsList
				.contains(message("forgot_password.invalid_email")));
	}

	@Test
	public void should_accept_recovery_for_existant_email() {
		UserFlow navigation = tryToSendResetPasswordEmail(user.getEmail());
		VRaptorTestResult sentEmail = navigation.followRedirect().execute();
		sentEmail.wasStatus(200).isValid();
		List<String> errorsList = errorsList(sentEmail);
		assertTrue(errorsList.isEmpty());
	}

	@Test
	public void should_loggin_for_reseted_password_user() {
		resetPassword();
		
		UserFlow navigation3 = loginWithPassword(navigate(), user.getEmail(), password);
		VRaptorTestResult loginResult = navigation3.followRedirect().execute();
		loginResult.wasStatus(200).isValid();
		
		LoggedUser loggedUser = loginResult.getObject("currentUser");
		assertEquals(user.getEmail(), loggedUser.getCurrent().getEmail());
	}

	@Test
	public void should_loggin_automatically_after_reseted_password_user() {
		VRaptorTestResult newPassword = resetPassword();

		LoggedUser logged = newPassword.getObject("currentUser");
		assertEquals(user.getEmail(), logged.getCurrent().getEmail());	
	}
	
	private VRaptorTestResult resetPassword() {
		UserFlow sentEmailNavigate = tryToSendResetPasswordEmail(user.getEmail());
		VRaptorTestResult sentEmail = sentEmailNavigate.followRedirect().execute();
		sentEmail.wasStatus(200).isValid();
		
		String urlFromPasswordToken = manager.getURLFromPasswordToken(user.getEmail());
		UserFlow newPasswordNavigate = tryToSetNewPassword(password, urlFromPasswordToken);
		VRaptorTestResult newPassword = newPasswordNavigate.followRedirect().execute();
		newPassword.wasStatus(200).isValid();
		return newPassword;
	}
	
	
	private UserFlow tryToSetNewPassword(String password, String urlFromPasswordToken) {
		String[] split = urlFromPasswordToken.split("/");
		return navigate().post(urlFromPasswordToken,
				initWith("id", split[2])
				.add("token", split[3])
				.add("password", password)
				.add("passwordConfirmation", password));
	}

	private UserFlow tryToSendResetPasswordEmail(String email) {
		return navigate().post("/esqueci-minha-senha",	initWith("email", email));
	}
}
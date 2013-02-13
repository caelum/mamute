package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class SignupTest extends AuthTestBase{

	@Test
	public void should_sign_up(){
		boolean loggedIn = home().toSignUpPage()
				.signUp("leonardo", "leo"+new Random().nextLong()+"@leo.com.br", "123456", "123456")
				.isLoggedInAs("leonardo");
		assertTrue(loggedIn);
	}
	
	@Test
	public void should_not_sign_up_with_invalid_email(){
		boolean loggedIn = home().toSignUpPage()
				.signUp("leonardo", "invalid_email", "123456", "123456")
				.isLoggedIn();
		assertFalse(loggedIn);
	}
	
	@Test
	public void should_not_sign_up_with_wrong_password(){
		boolean loggedIn = home().toSignUpPage()
				.signUp("leonardo", "leo"+new Random().nextLong()+"@leo.com.br", "123456", "wrong_pass")
				.isLoggedIn();
		assertFalse(loggedIn);
	}
	
	@Test
	public void should_not_sign_up_with_an_existent_email(){
		boolean loggedIn = home().toSignUpPage()
				.signUp("leonardo", "leo@leo.com.br", "123456", "123456")
				.isLoggedIn();
		assertFalse(loggedIn);
	}

}

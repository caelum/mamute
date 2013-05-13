package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.Test;

public class SignupTest extends AcceptanceTestBase {
    
    @After
    public void logout(){
        if (home().isLoggedIn())
            home().logOut();
    }

	@Test
	public void should_sign_up(){
		boolean loggedIn = home().toSignUpPage()
				.signUp("leonardo", randomEmail(), "123456", "123456")
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
				.signUp("leonardo", randomEmail(), "123456", "wrong_pass")
				.isLoggedIn();
		assertFalse(loggedIn);
	}

	private String randomEmail() {
		return "leo"+new Random().nextLong()+"@leo.com.br";
	}
	
	@Test
	public void should_not_sign_up_with_an_existent_email(){
		boolean loggedIn = home().toSignUpPage()
				.signUp("leonardo", randomEmail(), "123456", "123456")
				.isLoggedIn();
		assertFalse(loggedIn);
	}
	
	@Test
	public void should_sign_up_through_login_page(){
		boolean loggedIn = home().toLoginPage()
					.signUp("leonardo", randomEmail(), "123456")
					.isLoggedIn();
		assertTrue(loggedIn);
	}

}

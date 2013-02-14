package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.integration.pages.NewQuestionPage;

public class AuthTest extends AuthTestBase{

	@Test
	public void should_log_in(){
		boolean loggedIn = home().toLoginPage()
		.login("leo@leo.com.br","123456")
		.isLoggedInAs("leonardo");
		assertTrue(loggedIn);
	}
	
	@Test
	public void should_not_log_in_with_invalid_user(){
		boolean loggedIn = home().toLoginPage()
				.login("invalid@invalid.com.br","123456")
				.isLoggedIn();
		assertFalse(loggedIn);
	}
	
	@Test
	public void should_save_url_when_redirected_to_login(){
		boolean hasRedirectUrl = home().toNewQuestionPageWhileNotLogged()
				.hasRedirectUrl(NewQuestionPage.URL);
		assertTrue(hasRedirectUrl);
	}
	
}

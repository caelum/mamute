package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.NewQuestionPage;

public class AuthTest extends AcceptanceTestBase {
    
    @After
    public void logout() {
        if (home().isLoggedIn())
            home().logOut();
    }

	@Test
	public void should_log_in(){
		logout();
		boolean loggedIn = home().toLoginPage()
			.login("leonardo.wolter@caelum.com.br","123456")
			.isLoggedInAs("Leonardo");
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

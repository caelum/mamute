package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertFalse;

import org.junit.After;

public class AuthTestBase extends AcceptanceTestBase{
	
	@After
	public void tryToLogOut(){
		if(home().isLoggedIn()){
			boolean loggedIn = home().logOut().isLoggedIn();
			assertFalse(loggedIn);
		}
	}	
}

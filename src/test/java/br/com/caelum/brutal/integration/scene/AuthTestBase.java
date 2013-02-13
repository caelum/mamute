package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertFalse;

public class AuthTestBase extends AcceptanceTestBase{
	protected void logOut(){
		boolean loggedIn = home().logOut().isLoggedIn();
		assertFalse(loggedIn);
	}	
}

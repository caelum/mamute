package org.mamute.integration.scene;

import org.junit.After;


public abstract class AuthenticatedAcceptanceTest extends AcceptanceTestBase {
    
    protected void login() {
        home().toLoginPage().login("acceptance@email.com.br", "123456");
    }
    
    @After
    public final void logout() {
    	driver.get(SERVER.getRoot() + "/logout");
    	home();
    }
    
    protected void loginRandomly() {
		logout();
		home().toSignUpPage()
            .signUp("acceptance test user", 
        		"acceptance"+ Math.random() +"@brutal.com", 
        		"123456", "123456");
    }
    
    protected void loginWithALotOfKarma() {
    	logout();
    	home().toLoginPage().login("karma.nigga@email.com.br", "123456");
    }
    
    protected void loginAsModerator() {
        logout();
        home().toLoginPage().login("moderator@email.com.br", "123456");
    }

}

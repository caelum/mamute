package br.com.caelum.brutal.integration.scene;

import org.junit.After;
import org.junit.Before;

public abstract class AuthenticatedAcceptanceTest extends AcceptanceTestBase {
    
    @Before
    public void login() {
        home().toLoginPage().login("acceptance@caelum.com.br", "123456");
    }
    
    @After
    public final void logout() {
        home().logOut();
    }
    
    protected void loginRandomly() {
        home().toSignUpPage()
                .signUp("acceptance test user", 
                        "acceptance"+ Math.random() +"@brutal.com", 
                        "123456", "123456");
    }

}

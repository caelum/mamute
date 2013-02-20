package br.com.caelum.brutal.integration.scene;

import java.util.Random;

import org.junit.After;
import org.junit.Before;

public abstract class AuthenticatedAcceptanceTest extends AcceptanceTestBase {
    
    @Before
    public final void login() {
        loginRandomly();
    }
    
    @After
    public final void logout() {
        home().logOut();
    }
    
    private void loginRandomly() {
        home().toSignUpPage()
                .signUp("chico sokol", 
                        "chico"+new Random().nextLong()+"@brutal.com", 
                        "123456", "123456");
    }

}

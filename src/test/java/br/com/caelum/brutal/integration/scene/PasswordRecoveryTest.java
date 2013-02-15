package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordRecoveryTest extends AcceptanceTestBase {
    
    @Test
    public void should_deny_recovery_for_inexistant_email() throws Exception {
        boolean sentEmail = home().toLoginPage().toForgotPasswordPage()
            .typeEmail("unexistant@brutal.com").resetPassword()
            .emailWasSent();
        assertFalse(sentEmail);
    }
    
    @Test
    public void should_accept_recovery_for_existant_email() throws Exception {
        boolean sentEmail = home().toLoginPage().toForgotPasswordPage()
                .typeEmail("francisco.sokol@caelum.com.br").resetPassword()
                .emailWasSent();
        assertTrue(sentEmail);
    }

}

package org.mamute.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mamute.integration.pages.ResetPasswordPage;
import org.mamute.integration.util.ServerInfo;
import org.mamute.providers.MamuteDatabaseConfiguration;
import org.mamute.providers.SessionFactoryCreator;

public class ForgotPasswordTest extends AcceptanceTestBase implements ServerInfo.AcceptanceTest  {
    
    private static Session SESSION;
    private String validEmail = "chico@email.com.br";

    @BeforeClass
    public static void setup() throws IOException {
        SessionFactoryCreator sessionFactoryCreator = new SessionFactoryCreator(new MamuteDatabaseConfiguration(env, null, null));
        sessionFactoryCreator.init();
        SessionFactory sf = sessionFactoryCreator.getInstance();
        SESSION = sf.openSession();
    }

    @Test
    public void should_deny_recovery_for_inexistant_email() throws Exception {
        boolean sentEmail = tryToSendResetPasswordEmail("unexistant@brutal.com");
        assertFalse(sentEmail);
    }
    
    @Test
    public void should_accept_recovery_for_existant_email() throws Exception {
        boolean sentEmail = tryToSendResetPasswordEmail(validEmail);
        assertTrue(sentEmail);
    }

    @Test
    public void should_show_new_password_form_for_reseted_password_user() throws Exception {
        tryToSendResetPasswordEmail(validEmail);
        tryToSetNewPassword("newpass");
        home().logOut();
        
        boolean isLoggedIn = home()
            .toLoginPage()
            .login(validEmail, "newpass")
            .isLoggedIn();
        
        assertTrue(isLoggedIn);
        home().logOut();
    }

    @Test
    public void should_login_automatically() throws Exception {
        tryToSendResetPasswordEmail(validEmail);
        tryToSetNewPassword("newpass");
        
        boolean isLoggedIn = home()
            .isLoggedIn();
        
        assertTrue(isLoggedIn);
        home().logOut();
    }

	private void tryToSetNewPassword(String newPass) {
        String recoverUrl = getLastRecoverURL();
		ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver, recoverUrl);
        resetPasswordPage.typePassword(newPass)
            .submitNewPassword();
	}

    private String getLastRecoverURL() {
    	SESSION.beginTransaction();
    	Query query = SESSION.createQuery("select u.id, u.forgotPasswordToken from User u where u.email=:email");
        Object[] result = (Object[]) query.setParameter("email", validEmail).uniqueResult();
        String recoverUrl = SERVER.urlFor("/mudar-senha/"+result[0]+"/"+result[1]);
        SESSION.getTransaction().commit();
        return recoverUrl;
    }

    private boolean tryToSendResetPasswordEmail(String email) {
        boolean sentEmail = home().toLoginPage().toForgotPasswordPage()
                .typeEmail(email).sendResetPassword()
                .emailWasSent();
        return sentEmail;
    }
}

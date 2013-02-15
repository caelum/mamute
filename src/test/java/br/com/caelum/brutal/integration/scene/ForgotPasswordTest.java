package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.ResetPasswordPage;
import br.com.caelum.brutal.providers.SessionFactoryCreator;
import br.com.caelum.pagpag.aceitacao.util.ServerInfo;
import br.com.caelum.vraptor.environment.Environment;

public class ForgotPasswordTest extends AcceptanceTestBase implements ServerInfo.TesteAceitacao  {
    
    private static Session SESSION;
    private String validEmail = "francisco.sokol@caelum.com.br";

    @BeforeClass
    public static void setup() {
        Environment env = mock(Environment.class);
        URL xml = ForgotPasswordTest.class.getClassLoader().getResource(HOMOLOG_ENV + "/hibernate.cfg.xml");
        assertNotNull(xml);
        when(env.getResource("/hibernate.cfg.xml")).thenReturn(xml);
        
        SessionFactoryCreator sessionFactoryCreator = new SessionFactoryCreator(env);
        SessionFactory sf = sessionFactoryCreator.getInstance();
        Session session = sf.openSession();
        SESSION = session;
    }
    
    @Test
    public void should_deny_recovery_for_inexistant_email() throws Exception {
        boolean sentEmail = tryToResetPassword("unexistant@brutal.com");
        assertFalse(sentEmail);
    }

    
    @Test
    public void should_accept_recovery_for_existant_email() throws Exception {
        boolean sentEmail = tryToResetPassword(validEmail);
        assertTrue(sentEmail);
    }
    
    @Test
    public void should_show_new_password_form_for_reseted_password_user() throws Exception {
        tryToResetPassword(validEmail);
        
        String recoverUrl = getRecoverURL();
        
        ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver, recoverUrl);
        resetPasswordPage
            .typePassword("newpass")
            .submitNewPassword();
        
        boolean isLoggedIn = home()
            .toLoginPage()
            .login(validEmail, "newpass")
            .isLoggedIn();
        
        assertTrue(isLoggedIn);
    }

    private String getRecoverURL() {
        Query query = SESSION.createQuery("select u.id,u.forgotPasswordToken from User u where u.email=:email");
        Object[] result = (Object[]) query.setParameter("email", validEmail).uniqueResult();
        String recoverUrl = SERVER.urlFor("/newpassword/"+result[0]+"/"+result[1]);
        return recoverUrl;
    }

    private boolean tryToResetPassword(String email) {
        boolean sentEmail = home().toLoginPage().toForgotPasswordPage()
                .typeEmail(email).resetPassword()
                .emailWasSent();
        return sentEmail;
    }
}

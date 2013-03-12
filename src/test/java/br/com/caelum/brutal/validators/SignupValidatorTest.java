package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.util.test.JSR303MockValidator;
import br.com.caelum.vraptor.util.test.MockLocalization;

public class SignupValidatorTest extends TestCase {

    private UserDAO users;
    private Validator validator;
    private SignupValidator signupValidator;
	private UserValidator userValidator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private Localization localization;
    
    @Before
    public void setup() {
        users = mock(UserDAO.class);
        validator = new JSR303MockValidator();
        localization = new MockLocalization();
        messageFactory = new MessageFactory(localization);
        emailValidator = new EmailValidator(validator, users, messageFactory);
        userValidator = new UserValidator(validator, emailValidator, messageFactory);
        signupValidator = new SignupValidator(validator, userValidator, messageFactory);
    }

    @Test
    public void should_verify_email() {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(true);
        User user = user("nome muito grande ai meu deus", "used@gmail.com");
        boolean valid = signupValidator.validate(user, "123456", "123456");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_email_without_domain() {
    	User user = user("nome muito grande ai meu deus", "usedgmail.com");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_passwords() throws Exception {
        when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
        User user = user("nome muito grande ai meu deus", "valid@gmail.com");
        boolean valid = signupValidator.validate(user, "123456", "1234567");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_tiny_password() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	User user = user("nome muito grande ai meu deus", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, "123", "123");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_password() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	String password = 666*100 + "";
    	User user = user("nome", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, password, password);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_empty_name() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	User user = user("", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_name() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    	User user = user(name, "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_null() throws Exception {
        boolean valid = signupValidator.validate(null, "123", "1234");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_valid_user() throws Exception {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(false);
        User user = user("nome muito grande ai meu deus", "used@gmail.com");
        boolean valid = signupValidator.validate(user, "123456", "123456");
        
        assertTrue(valid);
    }

}

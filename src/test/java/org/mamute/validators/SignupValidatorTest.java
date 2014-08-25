package org.mamute.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ResourceBundle;

import javax.validation.Validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mamute.controllers.BrutalValidator;
import org.mamute.dao.TestCase;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.User;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.Validator;

@RunWith(MockitoJUnitRunner.class)
public class SignupValidatorTest extends TestCase {

    private static final String VALID_EMAIL = "valid@gmail.com";
	private static final String VALID_USER_NAME = "valid user valid user";
	private static final String VALID_PASSWORD = "123456";
	private Validator validator;
    private SignupValidator signupValidator;
	private UserValidator userValidator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	@Mock private UserDAO users;
	@Mock private ResourceBundle bundle;
    
    @Before
    public void setup() {
        validator = new MockValidator();
        messageFactory = new MessageFactory(bundle);
        emailValidator = new EmailValidator(validator, users, messageFactory);
        javax.validation.Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
        BrutalValidator brutalValidator = new BrutalValidator(javaxValidator, validator, messageFactory);
        userValidator = new UserValidator(validator, emailValidator, messageFactory, brutalValidator);
        signupValidator = new SignupValidator(validator, userValidator, messageFactory, users);
        when(users.existsWithEmail(VALID_EMAIL)).thenReturn(false);
        when(users.existsWithName(VALID_USER_NAME)).thenReturn(false);
    }

    @Test
    public void should_verify_email() {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(true);
        User user = user(VALID_USER_NAME, "used@gmail.com");
        boolean valid = signupValidator.validate(user, VALID_PASSWORD, VALID_PASSWORD);
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_email_without_domain() {
    	User user = user("nome muito grande ai meu deus", "invalidgmail.com");
    	boolean valid = signupValidator.validate(user, VALID_PASSWORD, VALID_PASSWORD);
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_passwords() throws Exception {
        when(users.existsWithEmail(VALID_EMAIL)).thenReturn(false);
        User user = user(VALID_USER_NAME, VALID_EMAIL);
        boolean valid = signupValidator.validate(user, "123456", "1234567");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_tiny_password() throws Exception {
    	when(users.existsWithEmail(VALID_EMAIL)).thenReturn(false);
    	User user = user(VALID_USER_NAME, VALID_EMAIL);
    	boolean valid = signupValidator.validate(user, "12", "12");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_password() throws Exception {
    	when(users.existsWithEmail(VALID_EMAIL)).thenReturn(false);
    	String password = 666*100 + "";
    	User user = user(VALID_USER_NAME, VALID_EMAIL);
    	boolean valid = signupValidator.validate(user, password, password);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_empty_name() throws Exception {
    	when(users.existsWithEmail(VALID_EMAIL)).thenReturn(false);
    	User user = user("", VALID_EMAIL);
    	boolean valid = signupValidator.validate(user, VALID_PASSWORD, VALID_PASSWORD);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_name() throws Exception {
    	when(users.existsWithEmail(VALID_EMAIL)).thenReturn(false);
    	String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    	User user = user(name, VALID_EMAIL);
    	boolean valid = signupValidator.validate(user, VALID_PASSWORD, VALID_PASSWORD);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_null() throws Exception {
        boolean valid = signupValidator.validate(null, VALID_PASSWORD, VALID_PASSWORD);
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_null_password() throws Exception {
    	User user = user(VALID_USER_NAME, VALID_EMAIL);
    	boolean valid = signupValidator.validate(user, null, null);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_if_exists_user_with_same_name() throws Exception {
    	String name = "existentName";
    	when(users.existsWithName(name)).thenReturn(true);
		
    	User user = user(name, VALID_EMAIL);
		boolean valid = signupValidator.validate(user, VALID_PASSWORD, VALID_PASSWORD);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_valid_user() throws Exception {
        User user = user(VALID_USER_NAME, VALID_EMAIL);
        boolean valid = signupValidator.validate(user, VALID_PASSWORD, VALID_PASSWORD);
        
        assertTrue(valid);
    }

}

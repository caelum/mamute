package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.test.JSR303MockValidator;

public class UserValidatorTest {

    private UserDAO users;
    private Validator validator;
    private UserValidator userValidator;
    
    @Before
    public void setup() {
        users = mock(UserDAO.class);
        validator = new JSR303MockValidator();
        userValidator = new UserValidator(validator, users);
    }

    @Test
    public void should_verify_email() {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(true);
        User user = new User("nome", "used@gmail.com", "123");
        boolean valid = userValidator.validate(user, "123", "123");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_passwords() throws Exception {
        when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
        User user = new User("nome", "valid@gmail.com", "123");
        boolean valid = userValidator.validate(user, "123", "1234");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_null() throws Exception {
        boolean valid = userValidator.validate(null, "123", "1234");
        
        assertFalse(valid);
    }

}

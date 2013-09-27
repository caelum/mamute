package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ResourceBundle;

import javax.validation.Validation;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.controllers.BrutalValidator;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.dto.UserPersonalInfo;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.util.test.MockValidator;

public class UserPersonalInfoValidatorTest extends TestCase{

	private String validEmail;
	private Validator validator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private UserDAO users;
	private UserPersonalInfoValidator infoValidator;
	private BundleFormatter bundle;
	
	@Before
	public void setup() {
		this.bundle = mock(BundleFormatter.class);
		this.validEmail = "artur.adam@caelum.com.br";
		this.users = mock(UserDAO.class);
		this.validator = new MockValidator();
		this.messageFactory = new MessageFactory(mock(ResourceBundle.class));
		this.emailValidator = new EmailValidator(validator, users, messageFactory);
		javax.validation.Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
		BrutalValidator brutalValidator = new BrutalValidator(javaxValidator, validator);
		this.infoValidator = new UserPersonalInfoValidator(validator, emailValidator, messageFactory, bundle, brutalValidator);
	}

	@Test
	public void should_pass_validation_with_not_required_elements_null() {
		User artur = user("artur com seis caracteres", validEmail);
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName(artur.getName())
				.withEmail(artur.getEmail());
		
		assertTrue(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_under_twelve_years_old_user() {
		User artur = user("artur com seis caracteres", validEmail);
		DateTime hoje = DateTime.now();
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName(artur.getName())
				.withEmail(artur.getEmail())
				.withBirthDate(hoje);
		
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_user_trying_to_update_name_before_allowed_time() {
		User artur = user("artur com seis caracteres", validEmail);
		
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName("newName")
				.withEmail(artur.getEmail());
		
		when(bundle.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/YYYY");
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_validate_user_trying_to_update_name_after_allowed_time() {
		User artur = user("artur com seis caracteres", validEmail);
		
		DateTimeUtils.setCurrentMillisFixed(new DateTime().plusDays(31).getMillis());
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName("newName")
				.withEmail(artur.getEmail());
		
		when(bundle.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/YYYY");
		assertTrue(infoValidator.validate(info));
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	@Test
	public void should_not_validate_null_user(){
		UserPersonalInfo info = new UserPersonalInfo(null);
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_invalid_mail() {
		User artur = user("artur com seis caracteres", validEmail);
		
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withEmail("invalidEmail");
		
		assertFalse(infoValidator.validate(info));
	}
}

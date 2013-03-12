package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.dto.UserPersonalInfo;
import br.com.caelum.brutal.dto.UserPersonalInfoBuilder;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.util.test.JSR303MockValidator;

public class UserPersonalInfoValidatorTest extends TestCase{

	private String validEmail;
	private Validator validator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private Localization localization;
	private UserDAO users;
	private UserPersonalInfoValidator infoValidator;
	
	@Before
	public void setup() {
		this.validEmail = "artur.adam@caelum.com.br";
		this.users = mock(UserDAO.class);
		this.validator = new JSR303MockValidator();
		this.localization = mock(Localization.class);
		this.messageFactory = new MessageFactory(localization);
		this.emailValidator = new EmailValidator(validator, users, messageFactory);
		this.infoValidator = new UserPersonalInfoValidator(validator, emailValidator, messageFactory, localization);
	}

	@Test
	public void should_pass_validation_with_not_required_elements_null() {
		User artur = user("artur com seis caracteres", validEmail);
		UserPersonalInfo info = new UserPersonalInfoBuilder()
				.withUser(artur)
				.withName(artur.getName())
				.withEmail(artur.getEmail())
				.build();
		
		assertTrue(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_under_twelve_years_old_user() {
		User artur = user("artur com seis caracteres", validEmail);
		DateTime hoje = DateTime.now();
		UserPersonalInfo info = new UserPersonalInfoBuilder()
				.withUser(artur)
				.withName(artur.getName())
				.withEmail(artur.getEmail())
				.with(hoje)
				.build();
		
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_user_trying_to_update_name_before_allowed_time() {
		User artur = user("artur com seis caracteres", validEmail);
		
		UserPersonalInfo info = new UserPersonalInfoBuilder()
				.withUser(artur)
				.withName("newName")
				.withEmail(artur.getEmail())
				.build();
		
		when(localization.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/YYYY");
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_validate_user_trying_to_update_name_after_allowed_time() {
		User artur = user("artur com seis caracteres", validEmail);
		
		DateTimeUtils.setCurrentMillisFixed(new DateTime().plusDays(31).getMillis());
		UserPersonalInfo info = new UserPersonalInfoBuilder()
				.withUser(artur)
				.withName("newName")
				.withEmail(artur.getEmail())
				.build();
		
		when(localization.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/YYYY");
		assertTrue(infoValidator.validate(info));
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	@Test
	public void should_not_validate_null_user(){
		UserPersonalInfo info = new UserPersonalInfoBuilder()
				.withUser(null)
				.build();
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_invalid_mail() {
		User artur = user("artur com seis caracteres", validEmail);
		
		UserPersonalInfo info = new UserPersonalInfoBuilder()
				.withUser(artur)
				.withEmail("invalidEmail")
				.build();
		
		assertFalse(infoValidator.validate(info));
	}
}

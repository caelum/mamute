package org.mamute.converters;

import java.util.Locale;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mamute.factory.MessageFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.I18nParam;
import br.com.caelum.vraptor.validator.Validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class BrutalDateTimeConverterTest {

	@Mock
	private Validator validator;
	@Mock
	private MessageFactory messageFactory;
	@Mock
	private BundleFormatter bundle;

	private BrutalDateTimeConverter brutalDateTimeConverter;

	private I18nMessage invalidFormatMessage;
	private I18nMessage invalidDateMessage;

	@Before
	public void setUp() {
		brutalDateTimeConverter = new BrutalDateTimeConverter(Locale.ENGLISH, validator, messageFactory, bundle);

		Mockito.when(bundle.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/yyyy");

		invalidFormatMessage = new I18nMessage(new I18nParam("error"), "invalid format");
		Mockito.when(messageFactory.build("error", "converters.errors.invalid_date.format")).thenReturn(invalidFormatMessage);

		invalidDateMessage = new I18nMessage(new I18nParam("error"), "invalid date");
		Mockito.when(messageFactory.build("error", "converters.errors.invalid_date.parameters")).thenReturn(invalidDateMessage);
	}

	@Test
	public void converting_null_must_return_null() {
		assertNull(brutalDateTimeConverter.convert(null, DateTime.class));
	}

	@Test
	public void converting_empty_string_must_return_null() {
		assertNull(brutalDateTimeConverter.convert("", DateTime.class));
	}

	@Test
	public void converting_invalid_format_must_return_null() {
		Mockito.doReturn(Boolean.TRUE).when(validator).hasErrors();

		assertNull(brutalDateTimeConverter.convert("xx/yy/zzzz", DateTime.class));
	}

	@Test
	public void converting_invalid_format_must_add_validation_error() {
		Mockito.doReturn(Boolean.TRUE).when(validator).hasErrors();

		brutalDateTimeConverter.convert("xx/yy/zzzz", DateTime.class);

		Mockito.verify(validator).add(invalidFormatMessage);
	}

	@Test
	public void converting_invalid_date_must_return_null() {
		assertNull(brutalDateTimeConverter.convert("33/44/2000", DateTime.class));
	}

	@Test
	public void converting_invalid_date_must_add_validation_error() {
		brutalDateTimeConverter.convert("33/44/2000", DateTime.class);

		Mockito.verify(validator).add(invalidDateMessage);
	}

	@Test
	public void converting_must_return_parsed_date() {
		final DateTime result = brutalDateTimeConverter.convert("25/12/2013", DateTime.class);

		assertNotNull(result);
		assertEquals(2013, result.getYear());
		assertEquals(12, result.getMonthOfYear());
		assertEquals(25, result.getDayOfMonth());
	}
}

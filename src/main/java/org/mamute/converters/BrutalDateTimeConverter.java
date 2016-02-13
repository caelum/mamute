package org.mamute.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Locale;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mamute.factory.MessageFactory;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.jodatime.DateTimeConverter;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.validator.Validator;

@Convert(DateTime.class)
@Specializes
public class BrutalDateTimeConverter extends DateTimeConverter{

	private Validator validator;
	private MessageFactory messageFactory;
	private BundleFormatter bundle;

	@Deprecated
	public BrutalDateTimeConverter() {
		super();
	}

	@Inject
	public BrutalDateTimeConverter(Locale locale, Validator validator, MessageFactory messageFactory, BundleFormatter bundle)
	{
		super(locale);
		this.validator = validator;
		this.messageFactory = messageFactory;
		this.bundle = bundle;
	}

	@Override
	public DateTime convert(String value, Class<? extends DateTime> type) {
		if (isNullOrEmpty(value)) {
			return null;
		}

		try {
			return getFormatter().parseDateTime(value);
		} catch (IllegalFieldValueException e) {
			validator.add(messageFactory.build("error", "converters.errors.invalid_date.parameters"));
		}
		catch (IllegalArgumentException e) {
			validator.add(messageFactory.build("error", "converters.errors.invalid_date.format"));
		}
		return null;
	}

	@Override
	protected DateTimeFormatter getFormatter() {
		return DateTimeFormat.forPattern(bundle.getMessage("date.joda.simple.pattern"));
	}
}

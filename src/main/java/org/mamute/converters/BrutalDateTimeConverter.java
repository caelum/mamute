package org.mamute.converters;

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

@Convert(DateTime.class) @Specializes
public class BrutalDateTimeConverter extends DateTimeConverter{

	@Inject private Validator validator;
	@Inject private MessageFactory messageFactory;
	@Inject private BundleFormatter bundle;

	@Override
	public DateTime convert(String value, Class<? extends DateTime> type) {
		
		if (value == null || value.isEmpty()){
			return null;
		}
		String[] splitedDate = value.split("/");
		if(splitedDate.length > 3 
				|| value.length() > 10 
				|| !value.matches("^\\d\\d/\\d\\d/\\d\\d\\d\\d$")) {
			validator.add(messageFactory.build("error", "converters.errors.invalid_date.format"));
		}
		
		if (validator.hasErrors()) {
			return null;
		}
		
		DateTimeFormatter pattern = DateTimeFormat.forPattern(bundle.getMessage("date.joda.simple.pattern"));
		try {
			DateTime date = pattern.parseDateTime(value);
			return date;
		}catch (IllegalFieldValueException e) {
			validator.add(messageFactory.build("error", "converters.errors.invalid_date.parameters"));
		}
		return null;
	}
}

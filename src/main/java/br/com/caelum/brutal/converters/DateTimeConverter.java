package br.com.caelum.brutal.converters;

import java.util.ResourceBundle;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.vraptor4.Convert;
import br.com.caelum.vraptor4.Converter;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.core.Localization;

@Convert(DateTime.class)
public class DateTimeConverter implements Converter<DateTime>{

	@Inject private Validator validator;
	@Inject private Localization localization;
	@Inject private MessageFactory messageFactory;

	@Override
	public DateTime convert(String value, Class<? extends DateTime> type,
			ResourceBundle bundle) {
		
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
		
		DateTimeFormatter pattern = DateTimeFormat.forPattern(localization.getMessage("date.joda.simple.pattern"));
		try {
			DateTime date = pattern.parseDateTime(value);
			return date;
		}catch (IllegalFieldValueException e) {
			validator.add(messageFactory.build("error", "converters.errors.invalid_date.parameters"));
		}
		return null;
	}

}

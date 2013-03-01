package br.com.caelum.brutal.converters;

import java.util.ResourceBundle;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.I18nMessage;

@Convert(DateTime.class)
public class DateTimeConverter implements Converter<DateTime>{

	private Validator validator;
	private Localization localization;

	public DateTimeConverter(Validator validator, Localization localization){
		this.validator = validator;
		this.localization = localization;
		
	}
	
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
			validator.add(new I18nMessage("error", "converters.errors.invalid_date.format"));
		}
		
		if (validator.hasErrors()) {
			return null;
		}
		
		DateTimeFormatter pattern = DateTimeFormat.forPattern(localization.getMessage("date.joda.simple.pattern"));
		try {
			DateTime date = pattern.parseDateTime(value);
			return date;
		}catch (IllegalFieldValueException e) {
			validator.add(new I18nMessage("error", "converters.errors.invalid_date.parameters"));
		}
		return null;
	}

}

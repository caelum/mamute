package br.com.caelum.brutal.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BrutalDateFormat {
	
	@Inject private Locale locale;
	@Inject private ResourceBundle bundle;
	
	public DateTimeFormatter getInstance (String pattern) {
		return DateTimeFormat.forPattern(bundle.getString(pattern)).withLocale(locale);
	}
}

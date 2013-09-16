package br.com.caelum.brutal.util;

import javax.inject.Inject;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.vraptor.core.Localization;

public class BrutalDateFormat {
	
	@Inject private Localization localization;
	
	public DateTimeFormatter getInstance (String pattern) {
		return DateTimeFormat.forPattern(localization.getMessage(pattern)).withLocale(localization.getLocale());
	}
}

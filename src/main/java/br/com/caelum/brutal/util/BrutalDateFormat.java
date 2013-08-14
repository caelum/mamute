package br.com.caelum.brutal.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.vraptor4.core.Localization;

@Component
public class BrutalDateFormat {
	
	private Localization localization;

	public BrutalDateFormat(Localization localization) {
		this.localization = localization;
	}
	
	public DateTimeFormatter getInstance (String pattern) {
		return DateTimeFormat.forPattern(localization.getMessage(pattern)).withLocale(localization.getLocale());
	}
}

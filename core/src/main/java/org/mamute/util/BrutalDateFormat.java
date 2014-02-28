package org.mamute.util;

import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.vraptor.simplemail.template.BundleFormatter;

public class BrutalDateFormat {
	
	@Inject private Locale locale;
	@Inject private BundleFormatter bundle;
	
	public DateTimeFormatter getInstance (String pattern) {
		return DateTimeFormat.forPattern(bundle.getMessage(pattern)).withLocale(locale);
	}
}

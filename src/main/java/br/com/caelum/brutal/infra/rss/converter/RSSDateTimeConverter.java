package br.com.caelum.brutal.infra.rss.converter;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RSSDateTimeConverter implements Converter {

	private static final DateTimeFormatter RSS_PATTERN = new DateTimeFormatterBuilder()
		.appendDayOfWeekShortText().appendLiteral(", ")
		.appendDayOfMonth(2).appendLiteral(" ")
		.appendMonthOfYearShortText().appendLiteral(" ")
		.appendYear(4,4).appendLiteral(" ")
		.appendHourOfDay(2).appendLiteral(":")
		.appendMinuteOfHour(2).appendLiteral(":")
		.appendSecondOfMinute(2).appendLiteral(" ")
		.appendTimeZoneOffset("0", false, 2, 2)
		.toFormatter().withLocale(Locale.US);

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return DateTime.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		writer.setValue(RSS_PATTERN.print((DateTime) source));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return RSS_PATTERN.parseDateTime(reader.getValue());
	}

}

package org.mamute.infra.rss;

import java.util.Locale;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public enum RSSType {
	ONDE_TRABALHAR {
		@Override
		public DateTimeFormatter getDateFormat() {
			return new DateTimeFormatterBuilder()
			.appendDayOfWeekShortText().appendLiteral(", ")
			.appendDayOfMonth(2).appendLiteral(" ")
			.appendMonthOfYearShortText().appendLiteral(" ")
			.appendYear(4,4).appendLiteral(" ")
			.appendHourOfDay(2).appendLiteral(":")
			.appendMinuteOfHour(2).appendLiteral(":")
			.appendSecondOfMinute(2).appendLiteral(" ")
			.appendTimeZoneOffset("0", false, 2, 2)
			.toFormatter().withLocale(Locale.US);
		}
	},
	INFO_Q {
		@Override
		public DateTimeFormatter getDateFormat() {
			return new DateTimeFormatterBuilder()
			.appendDayOfWeekShortText().appendLiteral(", ")
			.appendDayOfMonth(2).appendLiteral(" ")
			.appendMonthOfYearShortText().appendLiteral(" ")
			.appendYear(4,4).appendLiteral(" ")
			.appendHourOfDay(2).appendLiteral(":")
			.appendMinuteOfHour(2).appendLiteral(":")
			.appendSecondOfMinute(2).appendLiteral(" ")
			.appendTimeZoneId()
			.toFormatter().withLocale(Locale.US);
		}
	};
	
	public abstract DateTimeFormatter getDateFormat();
}

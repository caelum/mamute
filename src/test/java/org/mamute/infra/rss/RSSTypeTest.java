package org.mamute.infra.rss;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.mamute.infra.rss.RSSType;

public class RSSTypeTest {

	@Test
	public void should_verify_date_format_onde_trabalhar() {
		DateTimeFormatter dateFormat = RSSType.ONDE_TRABALHAR.getDateFormat();
		LocalDateTime time = dateFormat.parseLocalDateTime("Wed, 27 Nov 2013 11:56:05 +0000");
		time(time);
	}

	@Test
	public void should_verify_date_format_info_q() {
		DateTimeFormatter dateFormat = RSSType.INFO_Q.getDateFormat();
		LocalDateTime time = dateFormat.parseLocalDateTime("Wed, 27 Nov 2013 11:56:05 GMT");
		time(time);
	}
	
	private void time(LocalDateTime time) {
		assertEquals(DateTimeConstants.WEDNESDAY, time.getDayOfWeek());
		assertEquals(27,time.getDayOfMonth());
		assertEquals(DateTimeConstants.NOVEMBER, time.getMonthOfYear());
		assertEquals(2013, time.getYear());
		assertEquals(11, time.getHourOfDay());
		assertEquals(56, time.getMinuteOfHour());
		assertEquals(5, time.getSecondOfMinute());
	}
}

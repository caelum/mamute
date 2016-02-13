package org.mamute.infra.rss.write;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RssEntryBuilder {

	private String name;
	private String title;
	private String link;
	private String guid;
	private DateTime date;
	private RssImageEntry image;
	private static final String PATTERN = "EEE, dd MMM yyy HH:mm:ss Z";
	static final DateTimeFormatter RSS_DATE_FORMATTER = DateTimeFormat.forPattern(PATTERN);

	public RssEntryBuilder withAuthor(String name) {
		this.name = name;
		return this;
	}

	public RssEntryBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public RssEntryBuilder withLink(String link) {
		this.link = link;
		return this;
	}

	public RssEntryBuilder withId(String guid) {
		this.guid = guid;
		return this;
	}
	
	public RssEntryBuilder withDate(DateTime date) {
		this.date = date;
		return this;
	}
	
	public RssEntryBuilder withImage(RssImageEntry image){
		this.image = image;
		return this;
	}

	public RssEntry build() {
		return new RssEntry(title, link, guid, RSS_DATE_FORMATTER.print(date), name, image);
	}
	
}

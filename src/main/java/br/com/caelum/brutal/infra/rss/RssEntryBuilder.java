package br.com.caelum.brutal.infra.rss;

import org.joda.time.DateTime;

public class RssEntryBuilder {

	private String name;
	private String title;
	private String link;
	private String guid;
	private DateTime date;

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

	public RssEntry build() {
		return new RssEntry(title, link, guid, date.toString());
	}
	
}

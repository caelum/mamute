package br.com.caelum.brutal.feed;

import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotation.XStreamUnmarshalling;

public class RSSItem {
	
	private final String title;
	private final DateTime pubDate;
	private final String guid;
	private final String link;
	private final String description;

	public RSSItem() {
		this(null, null, null, null, null);
	}
	
	@XStreamUnmarshalling
	public RSSItem(String title, String description, String link, DateTime pubDate, String guid) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.pubDate = pubDate;
		this.guid = guid;
	}

	public String getTitle() {
		return title;
	}

	public DateTime getPubDate() {
		return pubDate;
	}

	public String getGuid() {
		return guid;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "RSSItem [title=" + title + ", pubDate=" + pubDate + ", guid="
				+ guid + ", link=" + link + ", description=" + description
				+ "]";
	}
	
	
}

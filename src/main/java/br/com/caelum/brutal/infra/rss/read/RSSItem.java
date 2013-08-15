package br.com.caelum.brutal.infra.rss.read;

import org.joda.time.DateTime;

public class RSSItem {
	
	private String title;
	private DateTime pubDate;
	private String guid;
	private String link;
	private String description;

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

package org.mamute.infra.rss.read;

import java.util.List;

import org.joda.time.LocalDateTime;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class RSSItem {
	
	private String title;
	private LocalDateTime pubDate;
	private String guid;
	private String link;
	private String description;
	
	private List<String> category;
	@XStreamAlias("dc:creator")
	private String creator;
	@XStreamAlias("dc:date")
	private String date;
	@XStreamAlias("dc:identifier")
	private String identifier;
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getPubDate() {
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

	public List<String> getCategory() {
		return category;
	}
}

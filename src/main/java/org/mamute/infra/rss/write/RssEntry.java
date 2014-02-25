package org.mamute.infra.rss.write;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class RssEntry {

	private final String title;
	private final String link;
	private final String guid;
	private final String pubDate;
	private final String author;
	@XStreamAlias("enclosure")
	private final RssImageEntry image;

	public RssEntry(String title, String link, String guid, String pubDate, String authorName, RssImageEntry image) {
		this.title = title;
		this.link = link;
		this.guid = guid;
		this.pubDate = pubDate;
		this.author = authorName;
		this.image = image;
	}


}

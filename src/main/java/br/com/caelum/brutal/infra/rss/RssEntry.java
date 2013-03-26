package br.com.caelum.brutal.infra.rss;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class RssEntry {

	private final String title;
	private final String link;
	private final String guid;
	private final String pubDate;

	public RssEntry(String title, String link, String guid, String pubDate) {
		this.title = title;
		this.link = link;
		this.guid = guid;
		this.pubDate = pubDate;
	}


}

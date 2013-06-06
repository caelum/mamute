package br.com.caelum.brutal.infra.rss;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("image")
public class RssImageEntry {
	
	private final String title;
	private final String link;
	private final String url;
	
	public RssImageEntry(String title, String link, String url) {
		this.title = title;
		this.link = link;
		this.url = url;
	}

}

package br.com.caelum.brutal.feed;

import com.thoughtworks.xstream.annotation.XStreamUnmarshalling;

public class RSSImage {
	private final String url;
	private final String title;
	private final String link;

	public RSSImage() {
		this(null, null, null);
	}
	
	@XStreamUnmarshalling
	public RSSImage(String url, String title, String link) {
		this.url = url;
		this.title = title;
		this.link = link;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	@Override
	public String toString() {
		return "RSSImage [url=" + url + ", title=" + title + ", link=" + link
				+ "]";
	}

}

package org.mamute.infra.rss.read;


public class RSSImage {
	private String url;
	private String title;
	private String link;

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

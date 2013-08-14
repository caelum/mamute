package br.com.caelum.brutal.feed;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotation.XStreamUnmarshalling;

public class RSSChannel {
	private final String title;
	private final String description;
	private final String language;
	private final String webMaster;
	private final String managingEditor;
	private final String copyright;
	private final String docs;
	private final String ttl;
	private final RSSImage image;
	private final DateTime pubDate;
	private final String link;
	private final List<RSSItem> items;
	
	public RSSChannel() {
		this(null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	@XStreamUnmarshalling
	public RSSChannel(String title, String description, String language,
			String webMaster, String managingEditor, String copyright,
			String docs, String ttl, RSSImage image, DateTime pubDate,
			String link, ArrayList<RSSItem> items) {
		this.title = title;
		this.description = description;
		this.language = language;
		this.webMaster = webMaster;
		this.managingEditor = managingEditor;
		this.copyright = copyright;
		this.docs = docs;
		this.ttl = ttl;
		this.image = image;
		this.pubDate = pubDate;
		this.link = link;
		this.items = items;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public String getWebMaster() {
		return webMaster;
	}
	
	public String getManagingEditor() {
		return managingEditor;
	}
	
	public String getCopyright() {
		return copyright;
	}
	
	public String getDocs() {
		return docs;
	}
	
	public String getTtl() {
		return ttl;
	}
	
	public RSSImage getImage() {
		return image;
	}
	
	public DateTime getPubDate() {
		return pubDate;
	}
	
	public String getLink() {
		return link;
	}

	@Override
	public String toString() {
		return "RSSFeedChannel [title=" + title + ", description="
				+ description + ", language=" + language + ", webMaster="
				+ webMaster + ", managingEditor=" + managingEditor
				+ ", copyright=" + copyright + ", docs=" + docs + ", ttl="
				+ ttl + ", image=" + image + ", pubDate=" + pubDate + ", link="
				+ link + ", items=" + items + "]";
	}

	public List<RSSItem> getItems() {
		return items;
	}
	
}
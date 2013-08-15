package br.com.caelum.brutal.infra.rss.read;

import java.util.List;

import org.joda.time.DateTime;

public class RSSChannel {
	private String title;
	private String description;
	private String language;
	private String webMaster;
	private String managingEditor;
	private String copyright;
	private String docs;
	private String ttl;
	private RSSImage image;
	private DateTime pubDate;
	private String link;
	private List<RSSItem> items;
	
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

	public void limitItems(Integer numberOfItems) {
		items = getItems().subList(0, numberOfItems);		
	}
	
}
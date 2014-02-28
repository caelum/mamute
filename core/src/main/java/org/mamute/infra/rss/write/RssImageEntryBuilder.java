package org.mamute.infra.rss.write;

public class RssImageEntryBuilder {
	
	private String url;
	
	public RssImageEntryBuilder withUrl(String url){
		this.url = url;
		return this;
	}
	
	public RssImageEntry build(){
		return new RssImageEntry(url);
	}

}

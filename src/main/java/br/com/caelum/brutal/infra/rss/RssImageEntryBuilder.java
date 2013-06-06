package br.com.caelum.brutal.infra.rss;

public class RssImageEntryBuilder {
	
	private String title;
	private String url;
	private String link;
	
	public RssImageEntryBuilder withTitle(String title){
		this.title = title;
		return this;
	}
	
	public RssImageEntryBuilder withUrl(String url){
		this.url = url;
		return this;
	}
	
	public RssImageEntryBuilder withLink(String link){
		this.title = link;
		return this;
	}
	
	public RssImageEntry build(){
		return new RssImageEntry(title, link, url);
	}

}

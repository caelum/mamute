package br.com.caelum.brutal.infra.rss;

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

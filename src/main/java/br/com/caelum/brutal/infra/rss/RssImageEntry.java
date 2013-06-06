package br.com.caelum.brutal.infra.rss;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamConverter(value=RssImageEntryConverter.class)
public class RssImageEntry {
	
	private final String url;
	
	public RssImageEntry(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

}

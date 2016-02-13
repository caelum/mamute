package org.mamute.infra.rss.write;

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

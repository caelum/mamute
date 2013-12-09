package br.com.caelum.brutal.infra.rss.read;

import br.com.caelum.brutal.infra.rss.RSSType;
import br.com.caelum.brutal.infra.rss.converter.RSSDateTimeConverter;

import com.thoughtworks.xstream.XStream;

public class FeedConverter {
	private XStream xStream;

	public FeedConverter(RSSType rss) {
		xStream = new XStream();
		xStream.alias("rss", RSSFeed.class);
		xStream.alias("channel", RSSChannel.class);
		xStream.alias("image", RSSImage.class);
		xStream.alias("item", RSSItem.class);
		xStream.addImplicitCollection(RSSChannel.class, "items");
		xStream.addImplicitCollection(RSSItem.class, "category", String.class);
		xStream.processAnnotations(RSSItem.class);
		xStream.registerConverter(new RSSDateTimeConverter(rss.getDateFormat()));
	}
	
	public RSSFeed convert(String content){
		return (RSSFeed) xStream.fromXML(content);
	}
}

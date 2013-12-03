package br.com.caelum.brutal.infra.rss.read;

import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.brutal.infra.rss.converter.RSSDateTimeConverter;

import com.thoughtworks.xstream.XStream;

@ApplicationScoped
public class FeedConverter {
	private XStream xStream;

	public FeedConverter() {
		xStream = new XStream();
		xStream.alias("rss", RSSFeed.class);
		xStream.alias("channel", RSSChannel.class);
		xStream.alias("image", RSSImage.class);
		xStream.alias("item", RSSItem.class);
		xStream.addImplicitCollection(RSSChannel.class, "items");
		xStream.registerConverter(new RSSDateTimeConverter());
	}
	
	public RSSFeed convert(String content){
		return (RSSFeed) xStream.fromXML(content);
	}
}

package br.com.caelum.brutal.infra.rss.read;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;

import br.com.caelum.brutal.feed.converter.RSSDateTimeConverter;
import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.XStream;

@Component
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
	
	public RSSFeed convert(InputStream content) throws ClientProtocolException, IOException{
		return (RSSFeed) xStream.fromXML(content);
	}
}

package br.com.caelum.brutal.feed;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;
import org.junit.Test;

public class FeedConverterTest {

	@Test
	public void should_convert_channel_correctly() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter();
		RSSFeed feed = feedConverter.convert(FeedConverterTest.class.getResourceAsStream("/rss-example.xml"));
		RSSChannel channel = feed.getChannel();
		
		assertEquals("OndeTrabalhar.com", channel.getTitle());
		assertEquals("encontre vagas de emprego para trabalhar com o que deseja", channel.getDescription());
		assertEquals("pt-BR", channel.getLanguage());
		assertEquals("contato@ondetrabalhar.com", channel.getWebMaster());
		assertEquals("contato@ondetrabalhar.com", channel.getManagingEditor());
		assertEquals("Copyright 2013, Caelum - http://www.caelum.com.br", channel.getCopyright());
		assertEquals("http://blogs.law.harvard.edu/tech/rss", channel.getDocs());
		assertEquals("http://ondetrabalhar.com/vagas", channel.getLink());

		DateTime rssDay = new DateTime().withDate(2013, 8, 12);
		assertEquals(rssDay.getMonthOfYear(), channel.getPubDate().getMonthOfYear());
		assertEquals(rssDay.getYear(), channel.getPubDate().getYear());
		assertEquals(rssDay.getDayOfMonth(), channel.getPubDate().getDayOfMonth());
		
	}
	
	@Test
	public void should_convert_image_correctly() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter();
		RSSFeed feed = feedConverter.convert(FeedConverterTest.class.getResourceAsStream("/rss-example.xml"));
		RSSImage image = feed.getChannel().getImage();

		assertEquals("http://ondetralhar.com/images/logo-box.png?1247568237", image.getUrl());
		assertEquals("OndeTrabalhar.com", image.getTitle());
		assertEquals("http://ondetrabalhar.com/", image.getLink());
		
	}
	
	@Test
	public void should_convert_item_correctly() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter();
		RSSFeed feed = feedConverter.convert(FeedConverterTest.class.getResourceAsStream("/rss-example.xml"));
		RSSItem item = feed.getChannel().getItems().get(0);
		
		assertEquals("Senior Tech Leader", item.getTitle());
		assertEquals("http://ondetrabalhar.com/vagas/3649/senior-tech-leader", item.getLink());
		assertEquals("Fast growing Web Company based in São Paulo invested by Venture Capital funds is looking for an Senior Technical Leader to manage engineer teams.", item.getDescription());
		
		DateTime rssDay = new DateTime().withDate(2013, 8, 12);
		assertEquals(rssDay.getMonthOfYear(), item.getPubDate().getMonthOfYear());
		assertEquals(rssDay.getYear(), item.getPubDate().getYear());
		assertEquals("http://ondetrabalhar.com/vagas/3649/senior-tech-leader", item.getGuid());
		
	}

}

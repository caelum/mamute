package org.mamute.feed;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mamute.infra.rss.RSSType;
import org.mamute.infra.rss.read.FeedConverter;
import org.mamute.infra.rss.read.RSSChannel;
import org.mamute.infra.rss.read.RSSFeed;
import org.mamute.infra.rss.read.RSSImage;
import org.mamute.infra.rss.read.RSSItem;

public class FeedConverterTest {
	
	private static String rssOndeTrabalhar;
	private static String rssInfoQ;

	@BeforeClass
	public static void setup() throws IOException {
		rssOndeTrabalhar = IOUtils.toString(FeedConverterTest.class.getResourceAsStream("/rss-ondetrabalhar-example.xml"));
		rssInfoQ = IOUtils.toString(FeedConverterTest.class.getResourceAsStream("/rss-infoq-example.xml"));
	}

	@Test
	public void should_convert_channel_correctly_onde_trabalhar() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter(RSSType.ONDE_TRABALHAR);
		RSSFeed feed = feedConverter.convert(rssOndeTrabalhar);
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
	public void should_convert_image_correct_onde_trabalharly() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter(RSSType.ONDE_TRABALHAR);
		RSSFeed feed = feedConverter.convert(rssOndeTrabalhar);
		RSSImage image = feed.getChannel().getImage();

		assertEquals("http://ondetralhar.com/images/logo-box.png?1247568237", image.getUrl());
		assertEquals("OndeTrabalhar.com", image.getTitle());
		assertEquals("http://ondetrabalhar.com/", image.getLink());
		
	}
	
	@Test
	public void should_convert_item_correctly_onde_trabalhar() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter(RSSType.ONDE_TRABALHAR);
		RSSFeed feed = feedConverter.convert(rssOndeTrabalhar);
		RSSItem item = feed.getChannel().getItems().get(0);
		
		assertEquals("Senior Tech Leader", item.getTitle());
		assertEquals("http://ondetrabalhar.com/vagas/3649/senior-tech-leader", item.getLink());
		assertEquals("Fast growing Web Company based in São Paulo invested by Venture Capital funds is looking for an Senior Technical Leader to manage engineer teams.", item.getDescription());
		
		DateTime rssDay = new DateTime().withDate(2013, 8, 12);
		assertEquals(rssDay.getMonthOfYear(), item.getPubDate().getMonthOfYear());
		assertEquals(rssDay.getYear(), item.getPubDate().getYear());
		assertEquals("http://ondetrabalhar.com/vagas/3649/senior-tech-leader", item.getGuid());
	}
	
	@Test
	public void should_convert_channel_correctly_info_q() throws ClientProtocolException, IOException {
		FeedConverter feedConverter = new FeedConverter(RSSType.INFO_Q);
		RSSFeed feed = feedConverter.convert(rssInfoQ);
		RSSChannel channel = feed.getChannel();
		
		assertEquals("InfoQ Personalized Feed for Unregistered User - Register to upgrade!", channel.getTitle());
		assertEquals("http://www.infoq.com", channel.getLink());
		assertEquals("This RSS feed is a personalized feed, unique to your account on InfoQ.com.", channel.getDescription());
		RSSItem item = channel.getItems().get(0);
		assertEquals("Article: Applying Lean Thinking to Software Development", item.getTitle());
		assertEquals("http://www.infoq.com/articles/applying-lean-thinking-to-software-development", item.getLink());
		assertEquals("Scrum", item.getCategory().get(0));
		assertEquals("Lean", item.getCategory().get(1));
		
		DateTime rssDay = new DateTime().withDate(2013, 12, 5);
		assertEquals(rssDay.getMonthOfYear(), item.getPubDate().getMonthOfYear());
		assertEquals(rssDay.getYear(), item.getPubDate().getYear());
		assertEquals(rssDay.getDayOfMonth(), item.getPubDate().getDayOfMonth());
	}
}

package org.mamute.infra.rss.write;

import static org.mamute.infra.rss.write.RssEntryBuilder.RSS_DATE_FORMATTER;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.mamute.model.interfaces.RssContent;

import br.com.caelum.vraptor.environment.Environment;

public class RssFeedFactory {
	private QuestionRssEntryFactory entryFactory;
	private PrintStream stream;
	private String home;

	@Deprecated
	public RssFeedFactory() {
	}

	@Inject
	public RssFeedFactory(Environment env, QuestionRssEntryFactory questionRssEntryFactory) {
		this.home = env.get("host") + env.get("home.url");
		this.entryFactory = questionRssEntryFactory;
	}

	public void build(List<RssContent> rssContents, OutputStream output, String rssTitle, String rssDescription) {
		stream = new PrintStream(output);
		open(output, rssTitle, rssDescription);
		for (RssContent rssContent : rssContents) {
			entryFactory.writeEntry(rssContent, output);
			stream.print('\n');
		}
		close(output);
	}


	private void open(OutputStream output, String title, String description) {
		DateTime dateTime = new DateTime();
		stream.print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
				+ "<rss version=\"2.0\">\n"
				+ "<channel>\n"
				+ "<title>" + title + "</title>\n"
				+ "<description>" + description + "</description>\n"
				+ "<link>" + home + "</link>\n"
				+ "<lastBuildDate>" + RSS_DATE_FORMATTER.print(dateTime) + "</lastBuildDate>\n"
				+ "<pubDate>" + RSS_DATE_FORMATTER.print(dateTime) + "</pubDate>\n" 
				+ "<ttl>1800</ttl>\n\n");
	}

	private void close(OutputStream output) {
		stream.print("\n</channel>\n</rss>");
	}
}

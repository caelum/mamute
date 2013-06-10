package br.com.caelum.brutal.infra.rss;

import static br.com.caelum.brutal.infra.rss.RssEntryBuilder.RSS_DATE_FORMATTER;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class RssFeedFactory {
	private QuestionRssEntryFactory entryFactory;
	private PrintStream stream;
	private String home;
	
	public RssFeedFactory(Environment env, QuestionRssEntryFactory questionRssEntryFactory) {
		this.home = env.get("host") + env.get("home.url");
		this.entryFactory = questionRssEntryFactory;
	}

	public void build(List<Question> questions, OutputStream output) {
		stream = new PrintStream(output);
		open(output);
		for (Question question : questions) {
			entryFactory.writeEntry(question, output);
			stream.print('\n');
		}
		close(output);
	}


	private void open(OutputStream output) {
		DateTime dateTime = new DateTime();
		stream.print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
				+ "<rss version=\"2.0\">\n"
				+ "<channel>\n"
				+ "<title>GUJ respostas</title>\n"
				+ "<description>Últimas perguntas do GUJ respostas</description>\n"
				+ "<link>" + home + "</link>\n"
				+ "<lastBuildDate>" + RSS_DATE_FORMATTER.print(dateTime) + "</lastBuildDate>\n"
				+ "<pubDate>" + RSS_DATE_FORMATTER.print(dateTime) + "</pubDate>\n" 
				+ "<ttl>1800</ttl>\n\n");
	}

	private void close(OutputStream output) {
		stream.print("\n</channel>\n</rss>");
	}
}

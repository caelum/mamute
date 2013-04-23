package br.com.caelum.brutal.infra.rss;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Question;

public class RssFeedFactory {
	QuestionRssEntryFactory entryFactory = new QuestionRssEntryFactory();
	private PrintStream stream;

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
				+ "<link>http://guj.com.br/perguntas</link>\n"
				+ "<lastBuildDate>" + dateTime + "</lastBuildDate>\n"
				+ "<pubDate>" + dateTime + "</pubDate>\n" 
				+ "<ttl>1800</ttl>\n\n");
	}

	private void close(OutputStream output) {
		stream.print("\n</channel>\n</rss>");
	}
}

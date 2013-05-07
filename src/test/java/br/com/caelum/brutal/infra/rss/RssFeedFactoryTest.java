package br.com.caelum.brutal.infra.rss;

import static java.util.Arrays.asList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.joda.time.DateTimeUtils;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;

public class RssFeedFactoryTest extends TestCase {
	
	@Test
	public void should_generate_feed() throws IOException {
		RssFeedFactory rssFeedFactory = new RssFeedFactory();
		
		QuestionBuilder builder = new QuestionBuilder();
		
		DateTimeUtils.setCurrentMillisFixed(100);
		Question question1 = builder.withAuthor(user("author1", "author@email"))
			.withTitle("first question")
			.withDescription("question")
			.withId(1l)
			.build();
		
		Question question2 = builder.withId(2l)
			.withTitle("second question")
			.withAuthor(user("author2", "author@email"))
			.build();
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		rssFeedFactory.build(asList(question1, question2), output);
		output.close();
		String xml = new String(output.toByteArray());
		xml.contains("first question");
		xml.contains("second question");
		DateTimeUtils.setCurrentMillisSystem();
	}
}

package br.com.caelum.brutal.infra.rss;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.joda.time.DateTimeUtils;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

public class RssFeedFactoryTest extends TestCase {
	
	@Test
	public void should_generate_feed() throws IOException {
		RssFeedFactory rssFeedFactory = new RssFeedFactory();
		
		QuestionBuilder builder = new QuestionBuilder();
		
		DateTimeUtils.setCurrentMillisFixed(100);
		User user1 = user("author1", "author@email");
		Question question1 = builder.withAuthor(user1)
			.withTitle("first question")
			.withDescription("question")
			.withId(1l)
			.build();
		
		User user2 = user("author2", "author@email");
		Question question2 = builder.withId(2l)
			.withTitle("second question")
			.withAuthor(user2)
			.build();
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		rssFeedFactory.build(asList(question1, question2), output);
		output.close();
		String xml = new String(output.toByteArray());
		assertTrue(xml.contains("first question"));
		assertTrue(xml.contains("second question"));
		DateTimeUtils.setCurrentMillisSystem();
	}
}

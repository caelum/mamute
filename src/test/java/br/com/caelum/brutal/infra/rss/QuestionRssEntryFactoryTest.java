package br.com.caelum.brutal.infra.rss;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Scanner;

import org.joda.time.DateTimeUtils;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;

public class QuestionRssEntryFactoryTest extends TestCase {

	@Test
	public void should_create_entry_from_a_question() {
		QuestionRssEntryFactory factory = new QuestionRssEntryFactory();
		QuestionBuilder builder = new QuestionBuilder();
		DateTimeUtils.setCurrentMillisFixed(100);
		
		Question question = builder.withAuthor(user("author", "author@email"))
			.withTitle("question title")
			.withDescription("description")
			.withId(1l)
			.build();
		InputStream is = this.getClass().getResourceAsStream("/rss/entry_example.xml");
		String expected = new Scanner(is).useDelimiter("$$").next();
		String xml = factory.entryOf(question);
		
		assertEquals(expected, xml);
	}

}

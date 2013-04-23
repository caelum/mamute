package br.com.caelum.brutal.infra.rss;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.joda.time.DateTimeUtils;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;

public class QuestionRssEntryFactoryTest extends TestCase {

	@Test
	public void should_create_entry_from_a_question() throws IOException {
		QuestionRssEntryFactory factory = new QuestionRssEntryFactory();
		QuestionBuilder builder = new QuestionBuilder();
		DateTimeUtils.setCurrentMillisFixed(100);
		
		Question question = builder.withAuthor(user("author", "author@email"))
			.withTitle("question title")
			.withDescription("description")
			.withId(1l)
			.build();
		
		DateTimeUtils.setCurrentMillisSystem();
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		factory.writeEntry(question, output);
		output.close();
		String xml = new String(output.toByteArray());
		
		assertTrue(xml.contains("<link>http://guj.com.br/perguntas/1-question-title</link>"));
		assertTrue(xml.contains("<title>question title</title>"));
		assertTrue(xml.contains("<author>author</author>"));
	}

}

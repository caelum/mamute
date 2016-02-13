package org.mamute.infra.rss;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.infra.rss.write.QuestionRssEntryFactory;
import org.mamute.model.Question;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.EnvironmentType;

public class QuestionRssEntryFactoryTest extends TestCase {

	@Test
	public void should_create_entry_from_a_question() throws IOException {
		DefaultEnvironment env = new DefaultEnvironment(new EnvironmentType("mamute"));
		QuestionRssEntryFactory factory = new QuestionRssEntryFactory(env);
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
		assertTrue(xml.contains("<link>http://localhost:8080/1-question-title</link>"));
		assertTrue(xml.contains("<title><![CDATA[question title]]></title>"));
		assertTrue(xml.contains("<author><![CDATA[author]]></author>"));
	}

	@After
	public void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}

}

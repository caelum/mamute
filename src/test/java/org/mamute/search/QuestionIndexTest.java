package org.mamute.search;

import com.google.common.io.Resources;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.*;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class QuestionIndexTest extends SolrTestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionIndexTest.class);

	static QuestionIndex sut;
	static Map<Long, Question> questions;

	static User author;
	static QuestionBuilder question;
	static Tag eli5;
	static Tag eli12;
	static Tag science;
	static Tag hobby;
	static Tag tolstoy;

	@BeforeClass
	public static void setup() throws IOException, SolrServerException {
		author = new User("Leonardo", "leo@leo");
		author.confirmEmail();
		hibernateSession.save(author);

		sut = new QuestionIndex(solrServer);
		data();
	}

	@AfterClass
	public static void cleanup() throws IOException, SolrServerException {
		solrServer.deleteByQuery("*:*");
	}

	@Test
	public void should_find_questions_by_title() {
		List<Long> ids = sut.findQuestionsByTitle("sky blue", 3);
		assertEquals(1, ids.size());
		assertEquals("Why is the sky blue?", question(ids.get(0)).getTitle());
	}

	@Test
	public void should_find_questions_by_title_and_tag() {
		List<Long> ids = sut.findQuestionsByTitleAndTag("Where", Arrays.asList(eli12), 3);
		assertEquals(3, ids.size());
		assertEquals("Where do babies come from?", question(ids.get(0)).getTitle());
	}

	private Question question(Long id) {
		return questions.get(id);
	}

	private static void data() throws IOException, SolrServerException {
		questions = new HashMap<>();
		question = new QuestionBuilder();
		solrServer.deleteByQuery("*:*");

		eli5 = new Tag("eli5", "Explain Like I'm 5", author);
		hibernateSession.save(eli5);
		eli12 = new Tag("eli12", "Explain Like I'm 12", author);
		hibernateSession.save(eli12);
		science = new Tag("science", "Science!", author);
		hibernateSession.save(science);
		hobby = new Tag("hobby", "Hobbies and stuff", author);
		hibernateSession.save(hobby);
		tolstoy = new Tag("tolstoy", "Long-winded russian author", author);
		hibernateSession.save(tolstoy);

		Question q;
		try (BufferedReader fis = new BufferedReader(new InputStreamReader(Resources.getResource("index/war-peace.txt").openStream()))) {
			String line;
			long count = 1;

			//load first 500 lines of war and peace
			LOGGER.info("Warring for peace");
			while ((line = fis.readLine()) != null) {
				if (line.length() > QuestionInformation.TITLE_MIN_LENGTH &&
						line.length() < QuestionInformation.TITLE_MAX_LENGTH) {
					q = question
							.withId(count++)
							.withTitle(line)
							.withTags(Arrays.asList(tolstoy))
							.withAuthor(author)
							.build();
					questions.put(q.getId(), q);
				}
			}
			sut.indexQuestionBatch(questions.values());
			LOGGER.info("Peace achieved");

			q = question
					.withId(count++)
					.withTitle("Why is the sky blue?")
					.withTags(Arrays.asList(science, eli5))
					.withAuthor(author)
					.build();
			questions.put(q.getId(), q);
			sut.indexQuestion(q);

			q = question
					.withId(count++)
					.withTitle("Where do babies come from?")
					.withTags(Arrays.asList(science, eli12))
					.withAuthor(author)
					.build();
			questions.put(q.getId(), q);
			sut.indexQuestion(q);

			q = question
					.withId(count++)
					.withTitle("How do they get the ship in the bottle?")
					.withTags(Arrays.asList(hobby))
					.withAuthor(author)
					.build();
			questions.put(q.getId(), q);
			sut.indexQuestion(q);
		}
	}
}

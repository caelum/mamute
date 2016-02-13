package org.mamute.search;

import static org.junit.Assert.assertEquals;
import static org.mamute.model.SanitizedText.fromTrustedText;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;

public class SolrQuestionIndexTest extends SolrTestCase {
	private QuestionIndex sut = new SolrQuestionIndex(solrServer);
	private QuestionBuilder questionBuilder = new QuestionBuilder();

	private User author;
	private Tag eli5;
	private Tag eli12;
	private Tag science;
	private Tag hobby;
	private Question whyIsSkyBlue;
	private Question whereDoBabiesComeFrom;
	private Question howShipInBottle;

	@Before
	public void setup() throws IOException, SolrServerException, InterruptedException {
		author = new User(fromTrustedText("Leonardo"), "leo@leo");

		eli5 = new Tag("eli5", "Explain Like I'm 5", author);
		eli12 = new Tag("eli12", "Explain Like I'm 12", author);
		science = new Tag("science", "Science!", author);
		hobby = new Tag("hobby", "Hobbies and stuff", author);

		whyIsSkyBlue = createQuestion(1L, "Why is the sky blue?", "I wanna know why is sky blue", science, eli5);
		whereDoBabiesComeFrom = createQuestion(2L, "Where do babies come from?", "My mom said I came from a bird", science, eli12);
		howShipInBottle = createQuestion(3L, "How do they get the ship in the bottle?", "I wanna know this for my school work", hobby);
	}

	@Test
	public void should_find_questions_by_title() {
		List<Long> ids = sut.find("sky blue", 1);
		assertEquals(1, ids.size());
		assertEquals(whyIsSkyBlue.getId(), ids.get(0));
	}

	@Test
	public void should_find_questions_by_tag() {
		List<Long> ids = sut.find(eli12.getName(), 1);
		assertEquals(1, ids.size());
		assertEquals(whereDoBabiesComeFrom.getId(), ids.get(0));
	}

	@Test
	public void should_find_questions_by_description() {
		List<Long> ids = sut.find("school work", 1);
		assertEquals(1, ids.size());
		assertEquals(howShipInBottle.getId(), ids.get(0));
	}

	@Test
	public void should_find_questions_with_different_tense() {
		List<Long> ids = sut.find("working", 1);
		assertEquals(1, ids.size());
		assertEquals(howShipInBottle.getId(), ids.get(0));
	}

	@Test
	public void should_find_questions_with_plural() {
		List<Long> ids = sut.find("skies", 1);
		assertEquals(1, ids.size());
		assertEquals(whyIsSkyBlue.getId(), ids.get(0));
	}

	@Test
	public void should_escape_solr_special_characters() {
		sut.find(" [JAVA] Erro multipart/form-data usando primefaces JSF FileUpload", 1);
	}

	private Question createQuestion(Long id, String title, String description, Tag... tags) {
		Question question = questionBuilder
				.withId(id)
				.withTitle(title)
				.withDescription(description)
				.withTags(Arrays.<Tag>asList(tags))
				.withAuthor(author)
				.build();
		sut.indexQuestion(question);
		return question;
	}
}

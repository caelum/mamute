package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mamute.dao.QuestionDAO.PAGE_SIZE;
import static org.mamute.model.MarkedText.notMarked;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import net.vidageek.mirror.dsl.Mirror;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.LoggedUser;
import org.mamute.model.MarkedText;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.interfaces.RssContent;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

import com.google.common.collect.ImmutableList;

public class QuestionDAOTest extends DatabaseTestCase {

	private static final String INVALID_TITLE = "Tiny title";
	private static final MarkedText INVALID_DESC = notMarked("Tiny desc");
	private static final MarkedText VALID_DESC = notMarked("Description with more than 30 characters");
	private static final String VALID_TITLE = "Title with more than 15 characters";
	private QuestionDAO questionsBeingAuthor;
	private QuestionBuilder question = new QuestionBuilder();
	private User author;
	private Tag java = tag("java");
	private Tag defaultTag = tag("defaultTag");
	private QuestionDAO questionsBeingModerator;
	private User moderator;
	private User anyone;
	private QuestionDAO questionsForAnyone;

	@Before
	public void setup() {
		author = user("Leonardo", "leo@leo");
		moderator = user("Leonardo", "leo@leo").asModerator();
		anyone = user("Leonardo", "leo@leo");
		session.save(author);
		session.save(moderator);
		session.save(anyone);
		session.save(java);
		session.save(defaultTag);
		this.questionsBeingAuthor = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(author, null)));
		this.questionsBeingModerator = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(moderator, null)));
		this.questionsForAnyone = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(anyone, null)));
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_is_null() {
		Question myQuestion = question.withTitle(VALID_TITLE).withDescription((String) null).withAuthor(author).build();
		questionsBeingAuthor.save(myQuestion );
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_has_less_than_30_chars() {
		Question myQuestion = question.withTitle(VALID_TITLE).withDescription(INVALID_DESC).withAuthor(author).build();
		questionsBeingAuthor.save(myQuestion);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_is_null() {
		Question myQuestion = question.withTitle(null).withDescription(VALID_DESC).withAuthor(author).build();
		questionsBeingAuthor.save(myQuestion);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_has_less_than_15_chars() {
		Question myQuestion = question.withTitle(INVALID_TITLE).withDescription(VALID_DESC).withAuthor(author).build();
		questionsBeingAuthor.save(myQuestion );
	}
	
    
	@Test
	public void should_return_only_questions_with_the_provided_tag() {
		Question javaQuestion = javaQuestion();
		Question javaEEQuestion = javaEEQuestion();
		Question androidQuestion = androidQuestion();
		
		List<Question> questionsAboutJava = questionsBeingAuthor.withTagVisible(java, 1);

		assertTrue(questionsAboutJava.contains(javaQuestion));
		assertFalse(questionsAboutJava.contains(javaEEQuestion));
		assertFalse(questionsAboutJava.contains(androidQuestion));
	}

	@Test
	public void should_not_ignore_invisible_ones_if_user_is_author() {
		Question javaQuestion = javaQuestion();
		assertContains(javaQuestion, questionsBeingAuthor);

		javaQuestion.remove();
		assertContains(javaQuestion, questionsBeingAuthor);
	}
	
	@Test
	public void should_not_ignore_invisible_ones_if_user_is_moderator() {
		Question javaQuestion = javaQuestion();
		assertContains(javaQuestion, questionsBeingModerator);
		
		javaQuestion.remove();
		assertContains(javaQuestion, questionsBeingModerator);
	}
	
	@Test
	public void should_ignore_invisible_ones_if_user_is_not_moderator() {
		Question javaQuestion = javaQuestion();
		assertContains(javaQuestion, questionsForAnyone);
		
		javaQuestion.remove();
		assertNotContains(javaQuestion, questionsForAnyone);
	}
	
	@Test
	public void should_calculate_number_of_pages() {
		saveQuestions(2*PAGE_SIZE);
		assertEquals(2l, questionsForAnyone.numberOfPages());
		saveQuestions(1);
		assertEquals(3l, questionsForAnyone.numberOfPages());
		saveQuestions(PAGE_SIZE-1);
		assertEquals(3l, questionsForAnyone.numberOfPages());
	}
	
	@Test
	public void should_calculate_number_of_pages_by_tags() {
		saveQuestions(2*PAGE_SIZE, java);
		assertEquals(2l, questionsForAnyone.numberOfPages(java));
		saveQuestions(2*PAGE_SIZE, defaultTag, java);
		assertEquals(4l, questionsForAnyone.numberOfPages(java));
		assertEquals(2l, questionsForAnyone.numberOfPages(defaultTag));
	}

	@Test
	public void should_find_questions_visible_and_order_by_creation_date() throws Exception {
		Question question1 = question(author, java);
		Question question2 = question(author, java);
		question2.remove();
		session.save(question1);
		session.save(question2);
		List<RssContent> questions = questionsForAnyone.orderedByCreationDate(5);
		assertEquals(1, questions.size());
	}
	
	@Test
	public void should_find_questions_visible_and_order_by_creation_date_of_a_tag() throws Exception {
		Question question1 = question(author, java);
		Question question2 = question(author, defaultTag);
		Question invisible = question(author, defaultTag);
		invisible.remove();
		session.save(question1);
		session.save(question2);
		session.save(invisible);
		List<RssContent> questions = questionsForAnyone.orderedByCreationDate(30, defaultTag);
		assertEquals(1, questions.size());
	}
	
	@Test
	public void should_find_hot_questions_ignoring_invisibles() throws Exception {
		DateTime pastWeek = new DateTime().minusWeeks(1);
		Question oldQuestion = TimeMachine.goTo(pastWeek.minusDays(1)).andExecute(new Block<Question>() {
			@Override
			public Question run() {
				Question question = question(author, java);
				setVoteCount(question, 100);
				return question;
			}
		});
		Question withTenVotes = question(author, java);
		Question withFiveVotes = question(author, java);
		Question withNoVotes = question(author, java);
		Question invisible = question(author, java);
		setVoteCount(withTenVotes, 10);
		setVoteCount(withFiveVotes, 5);
		setVoteCount(invisible, 15);
		invisible.remove();
		
		session.save(oldQuestion);
		session.save(withTenVotes);
		session.save(withFiveVotes);
		session.save(withNoVotes);
		session.save(invisible);
		session.flush();
		
		List<Question> questions = questionsForAnyone.hot(pastWeek, 3);
		assertThat(questions, Matchers.containsInAnyOrder(withFiveVotes, withTenVotes, withNoVotes));
	}

	@Test
	public void should_get_question_list_without_invisible_ones(){
		Question q1 = question(author, java);
		Question q2 = question(author, java);
		Question q3 = question(author, java);
		q3.remove();
		session.save(q1);
		session.save(q2);
		session.save(q3);

		List<Question> questions = questionsForAnyone.allVisibleByIds(ImmutableList.of(q2.getId(), q3.getId(), q1.getId()));

		assertNotNull(questions);
		assertEquals(2, questions.size());
		assertEquals(q2.getId(), questions.get(0).getId());
		assertEquals(q1.getId(), questions.get(1).getId());
	}

	@Test
	public void should_get_empty_list(){
		List<Question> questions = questionsForAnyone.allVisibleByIds(Collections.<Long>emptyList());
		assertNotNull(questions);
		assertEquals(0, questions.size());
	}

	private void setVoteCount(Question withTenVotes, long count) {
		new Mirror().on(withTenVotes).set().field("voteCount").withValue(count);
	}

	private void saveQuestions(int total, Tag... tags) {
		for (int i = 0; i < total; i++) {
			questionsForAnyone.save(question(author, tags));
		}
	}
	
	private void assertNotContains(Question question, QuestionDAO dao) {
		assertFalse(dao.allVisible(1).contains(question));
		assertFalse(dao.unsolvedVisible(1).contains(question));
		assertFalse(dao.withTagVisible(java, 1).contains(question));
	}
	
	private void assertContains(Question question, QuestionDAO dao) {
		assertTrue(dao.allVisible(1).contains(question));
		assertTrue(dao.unsolvedVisible(1).contains(question));
		assertTrue(dao.withTagVisible(java, 1).contains(question));
	}
	
	private Question javaEEQuestion(){
		Question q = question
			.withTitle("Some question about java ee and other stuff?")
			.withDescription(notMarked("Please help solving my question about java ee! Thanks, guys!"))
			.withAuthor(author)
			.withTag(defaultTag)
			.build();
		session.save(q);
		return q;
	}
	
	private Question androidQuestion(){
		Question q = question
				.withTitle("Some question about android and other stuff?")
				.withDescription(notMarked("Please help solving my question about android! Thanks, guys!"))
				.withAuthor(author)
				.withTag(defaultTag)
				.build();
		session.save(q);
		return q;
		
	}

	private Question javaQuestion(){
		Question q =  question
				.withTitle("Some question about java SE and other stuff")
				.withDescription(notMarked("Please help solving my question about java! Thanks, guys!"))
				.withAuthor(author)
				.withTag(java)
				.build();
		session.save(q);
		return q;
	}
	
}

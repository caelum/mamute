package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public class QuestionDAOTest extends DatabaseTestCase {

	private static final String INVALID_TITLE = "Tiny title";
	private static final String INVALID_DESC = "Tiny desc";
	private static final String VALID_DESC = "Description with more than 30 characters";
	private static final String VALID_TITLE = "Title with more than 15 characters";
	private QuestionDAO questionsBeingAuthor;
	private QuestionBuilder question = new QuestionBuilder();
	private User author;
	private Tag sal = tag("sal");
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
		session.save(sal);
		session.save(defaultTag);
		this.questionsBeingAuthor = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(author, null)));
		this.questionsBeingModerator = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(moderator, null)));
		this.questionsForAnyone = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(anyone, null)));
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_is_null() {
		Question myQuestion = question.withTitle(VALID_TITLE).withDescription(null).withAuthor(author).build();
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
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_tags_is_empty() {
		Question myQuestion = question.withTags(new ArrayList<Tag>()).withTitle(VALID_TITLE).withDescription(VALID_DESC).withAuthor(author).build();
		questionsBeingAuthor.save(myQuestion );
	}
		
	@Test
	public void should_return_only_questions_with_the_provided_tag() {
		Question salDaAzar = salDaAzar();
		Question beberFazMal = beberFazMal();
		Question androidRuim = androidRuim();
		
		List<Question> perguntasComSal = questionsBeingAuthor.withTagVisible(sal, 1);

		assertTrue(perguntasComSal.contains(salDaAzar));
		assertFalse(perguntasComSal.contains(beberFazMal));
		assertFalse(perguntasComSal.contains(androidRuim));
	}

	@Test
	public void should_not_ignore_invisible_ones_if_user_is_author() {
		Question salDaAzar = salDaAzar();
		assertContains(salDaAzar, questionsBeingAuthor);

		salDaAzar.remove();
		assertContains(salDaAzar, questionsBeingAuthor);
	}
	
	@Test
	public void should_not_ignore_invisible_ones_if_user_is_moderator() {
		Question salDaAzar = salDaAzar();
		assertContains(salDaAzar, questionsBeingModerator);
		
		salDaAzar.remove();
		assertContains(salDaAzar, questionsBeingModerator);
	}
	
	@Test
	public void should_ignore_invisible_ones_if_user_is_not_moderator() {
		Question salDaAzar = salDaAzar();
		assertContains(salDaAzar, questionsForAnyone);
		
		salDaAzar.remove();
		assertNotContains(salDaAzar, questionsForAnyone);
	}
	
	@Test
	public void should_list_question_with_low_vote_count_in_tag_listing() {
		Question salDaAzar = salDaAzar();
		assertTrue(questionsBeingAuthor.allVisible(1).contains(salDaAzar));
		assertTrue(questionsBeingAuthor.withTagVisible(sal, 1).contains(salDaAzar));
		
		session.createQuery("update Question as q set q.voteCount = -4").executeUpdate();
		assertTrue(questionsBeingAuthor.allVisible(1).contains(salDaAzar));
		assertTrue(questionsBeingAuthor.withTagVisible(sal, 1).contains(salDaAzar));
		
		session.createQuery("update Question as q set q.voteCount = -5").executeUpdate();
		assertFalse(questionsBeingAuthor.allVisible(1).contains(salDaAzar));
		assertTrue(questionsBeingAuthor.withTagVisible(sal, 1).contains(salDaAzar));
	}
	
	@Test
	public void should_calculate_number_of_pages() {
		saveQuestions(100);
		assertEquals(2l, questionsForAnyone.numberOfPages());
		saveQuestions(1);
		assertEquals(3l, questionsForAnyone.numberOfPages());
		saveQuestions(49);
		assertEquals(3l, questionsForAnyone.numberOfPages());
	}
	
	@Test
	public void should_calculate_number_of_pages_by_tags() {
		saveQuestions(100, sal);
		assertEquals(2l, questionsForAnyone.numberOfPages(sal));
		saveQuestions(100, defaultTag, sal);
		assertEquals(4l, questionsForAnyone.numberOfPages(sal));
		assertEquals(2l, questionsForAnyone.numberOfPages(defaultTag));
	}

	@Test
	public void should_find_questions_visible_and_order_by_creation_date() throws Exception {
		Question question1 = question(author, sal);
		Question question2 = question(author, sal);
		question2.remove();
		session.save(question1);
		session.save(question2);
		List<Question> questions = questionsForAnyone.orderedByCreationDate(5);
		assertEquals(1, questions.size());
	}
	
	@Test
	public void should_find_questions_visible_and_order_by_creation_date_of_a_tag() throws Exception {
		Question question1 = question(author, sal);
		Question question2 = question(author, defaultTag);
		Question invisible = question(author, defaultTag);
		invisible.remove();
		session.save(question1);
		session.save(question2);
		session.save(invisible);
		List<Question> questions = questionsForAnyone.orderedByCreationDate(30, defaultTag);
		assertEquals(1, questions.size());
	}

	private void saveQuestions(int total, Tag... tags) {
		for (int i = 0; i < total; i++) {
			questionsForAnyone.save(question(author, tags));
		}
	}
	
	private void assertNotContains(Question salDaAzar, QuestionDAO dao) {
		assertFalse(dao.allVisible(1).contains(salDaAzar));
		assertFalse(dao.unsolvedVisible(1).contains(salDaAzar));
		assertFalse(dao.withTagVisible(sal, 1).contains(salDaAzar));
	}
	
	private void assertContains(Question salDaAzar, QuestionDAO dao) {
		assertTrue(dao.allVisible(1).contains(salDaAzar));
		assertTrue(dao.unsolvedVisible(1).contains(salDaAzar));
		assertTrue(dao.withTagVisible(sal, 1).contains(salDaAzar));
	}
	
	private Question beberFazMal(){
		Question beberFazMal = question
			.withTitle("Por que dizem que beber demais faz mal?")
			.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
			.withAuthor(author)
			.withTag(defaultTag)
			.build();
		session.save(beberFazMal);
		return beberFazMal;
	}
	
	private Question androidRuim(){
		Question androidRuim = question
				.withTitle("Por que a api de android é tão ruim?")
				.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
				.withAuthor(author)
				.withTag(defaultTag)
				.build();
		session.save(androidRuim);
		return androidRuim;
		
	}

	private Question salDaAzar(){
		Question salDaAzar =  question
				.withTitle("Por que pegar o sal da mal dos outros da azar?")
				.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
				.withAuthor(author)
				.withTag(sal)
				.build();
		session.save(salDaAzar);
		return salDaAzar;
	}
	
}

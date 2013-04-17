package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

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
		this.questionsBeingAuthor = new QuestionDAO(session, new InvisibleForUsersRule(author));
		this.questionsBeingModerator = new QuestionDAO(session, new InvisibleForUsersRule(moderator));
		this.questionsForAnyone = new QuestionDAO(session, new InvisibleForUsersRule(anyone));
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
		
		List<Question> perguntasComSal = questionsBeingAuthor.withTagVisible(sal);

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
		for (int i = 0; i < -QuestionDAO.SPAM_BOUNDARY; i++) {
			Vote vote = new Vote(null, VoteType.DOWN);
			salDaAzar.substitute(null, vote);
			session.save(vote);
		}
		List<Question> all = questionsForAnyone.allVisible();
		List<Question> withTag = questionsForAnyone.withTagVisible(sal);
		
		assertFalse(all.contains(salDaAzar));
		assertTrue(withTag.contains(salDaAzar));
	}


	private void assertNotContains(Question salDaAzar, QuestionDAO dao) {
		assertFalse(dao.allVisible().contains(salDaAzar));
		assertFalse(dao.unsolvedVisible().contains(salDaAzar));
		assertFalse(dao.withTagVisible(sal).contains(salDaAzar));
	}
	
	private void assertContains(Question salDaAzar, QuestionDAO dao) {
		assertTrue(dao.allVisible().contains(salDaAzar));
		assertTrue(dao.unsolvedVisible().contains(salDaAzar));
		assertTrue(dao.withTagVisible(sal).contains(salDaAzar));
	}
	
	public Question beberFazMal(){
		Question beberFazMal = question
			.withTitle("Por que dizem que beber demais faz mal?")
			.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
			.withAuthor(author)
			.withTag(defaultTag)
			.build();
		session.save(beberFazMal);
		return beberFazMal;
	}
	
	public Question androidRuim(){
		Question androidRuim = question
				.withTitle("Por que a api de android é tão ruim?")
				.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
				.withAuthor(author)
				.withTag(defaultTag)
				.build();
		session.save(androidRuim);
		return androidRuim;
		
	}

	public Question salDaAzar(){
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

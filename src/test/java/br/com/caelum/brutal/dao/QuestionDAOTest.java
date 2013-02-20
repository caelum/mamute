package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

public class QuestionDAOTest extends DatabaseTestCase {

	private QuestionDAO questions;
	private TagDAO tags;
	private User author;
	private VoteDAO votes;

	@Before
	public void setup() {
		author = new User("Leonardo", "leo@leo", "123456");
		session.save(author);
		this.questions = new QuestionDAO(session);
		this.tags = new TagDAO(session);
		this.votes = new VoteDAO(session);
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_is_null() {
		Question question = question("Title with more than 15 characters", null, author);
		questions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_has_less_than_30_chars() {
		Question question = question("Title with more than 15 characters", "Tiny desc", author);
		questions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_is_null() {
		Question question = question(null, "Description with more than 30 characters", author);
		questions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_has_less_than_15_chars() {
		Question question = question("Tiny title", "Description with more than 30 characters", author);
		questions.save(question);
	}
	
	
	@Test
	public void should_ignore_low_reputation_ones() {
		Question salDaAzar = question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		questions.save(salDaAzar);
		
		assertTrue(questions.all().contains(salDaAzar));
		
		session.createQuery("update Question as q set q.voteCount = -4").executeUpdate();
		assertTrue(questions.all().contains(salDaAzar));

		session.createQuery("update Question as q set q.voteCount = -5").executeUpdate();
		assertFalse(questions.all().contains(salDaAzar));

	}
	
	@Test
	public void should_return_only_questions_with_the_provided_tag() {
		Tag sal = new Tag("sal", "", null);
		tags.saveOrLoad(sal);

		Question salDaAzar =  question("Por que pegar o sal da mal dos outros da azar?",
				"Alguem poderia me dizer o por que disso? Obrigado galera!", 
				author, sal);
		Question beberFazMal = question("Por que dizem que beber demais faz mal?",
				"Alguem poderia me dizer o por que disso? Obrigado galera!",
				author);
		Question androidRuim = question("Por que a api de android é tão ruim?",
				"Alguem poderia me dizer o por que disso? Obrigado galera!",
				author);
		questions.save(salDaAzar);
		questions.save(beberFazMal);
		questions.save(androidRuim);
		
		List<Question> perguntasComSal = questions.withTag(sal);

		assertTrue(perguntasComSal.contains(salDaAzar));
		assertFalse(perguntasComSal.contains(beberFazMal));
		assertFalse(perguntasComSal.contains(androidRuim));
	}
	
	@Test
	public void should_return_only_questions_with_the_provided_user_ordered_by_vote_count() {
		Question beberFazMal = question("Por que dizem que beber demais faz mal?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		beberFazMal.substitute(null, new Vote(author, VoteType.UP));
		beberFazMal.substitute(null, new Vote(author, VoteType.UP));
		
		Question androidRuim = question("Por que a api de android é tão ruim?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		androidRuim.substitute(null, new Vote(author, VoteType.UP));

		Question salDaAzar =  question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		
		questions.save(salDaAzar);
		questions.save(beberFazMal);
		questions.save(androidRuim);
		
		List<Question> perguntasDoAuthor = questions.withAuthorByVotes(author);

		assertTrue(perguntasDoAuthor.contains(salDaAzar));
		assertTrue(perguntasDoAuthor.contains(beberFazMal));
		assertTrue(perguntasDoAuthor.contains(androidRuim));
		assertEquals(perguntasDoAuthor.get(0), beberFazMal);
		assertEquals(perguntasDoAuthor.get(1), androidRuim);
		assertEquals(perguntasDoAuthor.get(2), salDaAzar);
	}
}

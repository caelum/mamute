package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public class QuestionDAOTest extends DatabaseTestCase {

	private QuestionDAO unmoderatedQuestions, moderatedQuestions, unloggedQuestions;
	private TagDAO tags;


	@Before
	public void setup() {
		User guilherme = new User("","", "").asModerator();
		User moderator = new User("","", "").asModerator();
		this.unloggedQuestions = new QuestionDAO(session, null);
		this.unmoderatedQuestions = new QuestionDAO(session, guilherme);
		this.moderatedQuestions = new QuestionDAO(session, moderator);
		this.tags = new TagDAO(session);
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_is_null() {
		Question question = new Question("Title with more than 15 characters", null);
		unmoderatedQuestions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_has_less_than_30_chars() {
		Question question = new Question("Title with more than 15 characters", "Tiny desc");
		unmoderatedQuestions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_is_null() {
		Question question = new Question(null, "Description with more than 30 characters");
		unmoderatedQuestions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_has_less_than_15_chars() {
		Question question = new Question("Tiny title", "Description with more than 30 characters");
		unmoderatedQuestions.save(question);
	}
	
	
	@Test
	public void should_ignore_low_reputation_ones() {
		Question salDaAzar = new Question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		unmoderatedQuestions.save(salDaAzar);
		
		assertTrue(unmoderatedQuestions.all().contains(salDaAzar));
		assertTrue(unloggedQuestions.all().contains(salDaAzar));
		
		session.createQuery("update Question as q set q.voteCount = -4").executeUpdate();
		assertTrue(unmoderatedQuestions.all().contains(salDaAzar));
		assertTrue(unloggedQuestions.all().contains(salDaAzar));

		session.createQuery("update Question as q set q.voteCount = -5").executeUpdate();
		assertFalse(unmoderatedQuestions.all().contains(salDaAzar));
		assertFalse(unloggedQuestions.all().contains(salDaAzar));

	}
	
	@Test
	public void should_ignore_low_reputation_ones_for_moderators() {
		Question salDaAzar = new Question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		unmoderatedQuestions.save(salDaAzar);
		
		assertTrue(moderatedQuestions.all().contains(salDaAzar));
		
		session.createQuery("update Question as q set q.voteCount = -9").executeUpdate();
		assertTrue(moderatedQuestions.all().contains(salDaAzar));

		session.createQuery("update Question as q set q.voteCount = -10").executeUpdate();
		assertFalse(moderatedQuestions.all().contains(salDaAzar));

	}
	
	@Test
	public void should_return_only_questions_with_the_provided_tag() {
		Question salDaAzar = new Question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		Question beberFazMal = new Question("Por que dizem que beber demais faz mal?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		Question androidRuim = new Question("Por que a api de android é tão ruim?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		unmoderatedQuestions.save(salDaAzar);
		unmoderatedQuestions.save(beberFazMal);
		unmoderatedQuestions.save(androidRuim);
		
		Tag sal = new Tag("sal", "", null);
		tags.saveOrLoad(sal);
		salDaAzar.addTag(sal);
		
		List<Question> perguntasComSal = unmoderatedQuestions.withTag(sal);

		assertTrue(perguntasComSal.contains(salDaAzar));
		assertFalse(perguntasComSal.contains(beberFazMal));
		assertFalse(perguntasComSal.contains(androidRuim));
		
		
	}
		
	
}

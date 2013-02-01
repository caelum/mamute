package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.pagpag.integracao.dao.DatabaseTestCase;

public class QuestionDAOTest extends DatabaseTestCase {

	private QuestionDAO questions;
	private TagDAO tags;


	@Before
	public void setup() {
		this.questions = new QuestionDAO(session);
		this.tags = new TagDAO(session);
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void shouldThrowConstraintExceptionIfDescriptionIsNull() {
		Question question = new Question("Title with more than 15 characters", null);
		questions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void shouldThrowConstraintExceptionIfDescriptionHasLessThan30Chars() {
		Question question = new Question("Title with more than 15 characters", "Tiny desc");
		questions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void shouldThrowConstraintExceptionIfTitleIsNull() {
		Question question = new Question(null, "Description with more than 30 characters");
		questions.save(question);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void shouldThrowConstraintExceptionIfTitleHasLessThan15Chars() {
		Question question = new Question("Tiny title", "Description with more than 30 characters");
		questions.save(question);
	}
	
	@Test
	public void shouldReturnOnlyQuestionsWithTheProvidedTag() {
		Question salDaAzar = new Question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		Question beberFazMal = new Question("Por que dizem que beber demais faz mal?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		Question androidRuim = new Question("Por que a api de android é tão ruim?", "Alguem poderia me dizer o por que disso? Obrigado galera!");
		questions.save(salDaAzar);
		questions.save(beberFazMal);
		questions.save(androidRuim);
		Tag sal = new Tag("sal", "", salDaAzar);
		tags.save(sal);
		
		List<Question> perguntasComSal = questions.withTag(sal);

		assertTrue(perguntasComSal.contains(salDaAzar));
		assertFalse(perguntasComSal.contains(beberFazMal));
		assertFalse(perguntasComSal.contains(androidRuim));
		
		
	}
}

package br.com.caelum.brutal.dao;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.pagpag.integracao.dao.DatabaseTestCase;

public class QuestionDAOTest extends DatabaseTestCase {

	private QuestionDAO questions;


	@Before
	public void setup() {
		this.questions = new QuestionDAO(session);
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

}

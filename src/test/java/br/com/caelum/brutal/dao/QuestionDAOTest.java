package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public class QuestionDAOTest extends DatabaseTestCase {

	private static final String INVALID_TITLE = "Tiny title";
	private static final String INVALID_DESC = "Tiny desc";
	private static final String VALID_DESC = "Description with more than 30 characters";
	private static final String VALID_TITLE = "Title with more than 15 characters";
	private QuestionDAO questions;
	private QuestionBuilder question = new QuestionBuilder();
	private User author;
	private Tag sal = tag("sal");
	private Tag defaultTag = tag("defaultTag");

	@Before
	public void setup() {
		author = user("Leonardo", "leo@leo");
		session.save(author);
		session.save(sal);
		session.save(defaultTag);
		this.questions = new QuestionDAO(session, new InvisibleForUsersRule(author));
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_is_null() {
		Question myQuestion = question.withTitle(VALID_TITLE).withDescription(null).withAuthor(author).build();
		questions.save(myQuestion );
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_description_has_less_than_30_chars() {
		Question myQuestion = question.withTitle(VALID_TITLE).withDescription(INVALID_DESC).withAuthor(author).build();
		questions.save(myQuestion);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_is_null() {
		Question myQuestion = question.withTitle(null).withDescription(VALID_DESC).withAuthor(author).build();
		questions.save(myQuestion);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_title_has_less_than_15_chars() {
		Question myQuestion = question.withTitle(INVALID_TITLE).withDescription(VALID_DESC).withAuthor(author).build();
		questions.save(myQuestion );
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_throw_constraint_exception_if_tags_is_empty() {
		Question myQuestion = question.withTags(new ArrayList<Tag>()).withTitle(VALID_TITLE).withDescription(VALID_DESC).withAuthor(author).build();
		questions.save(myQuestion );
	}
	
	@Test
	public void should_ignore_low_reputation_ones() {
		Question salDaAzar = salDaAzar();
		assertTrue(questions.allVisible().contains(salDaAzar));
		
		session.createQuery("update Question as q set q.voteCount = -4").executeUpdate();
		assertTrue(questions.allVisible().contains(salDaAzar));

		session.createQuery("update Question as q set q.voteCount = -5").executeUpdate();
		assertFalse(questions.allVisible().contains(salDaAzar));

	}
	
	@Test
	public void should_ignore_invisible_ones() {
		Question salDaAzar = salDaAzar();
		assertTrue(questions.allVisible().contains(salDaAzar));
		assertTrue(questions.unsolvedVisible().contains(salDaAzar));
		assertTrue(questions.withTagVisible(sal).contains(salDaAzar));

		salDaAzar.remove();
		assertFalse(questions.allVisible().contains(salDaAzar));
		assertFalse(questions.unsolvedVisible().contains(salDaAzar));
		assertFalse(questions.withTagVisible(sal).contains(salDaAzar));
	}
	
	@Test
	public void should_return_only_questions_with_the_provided_tag() {
		Question salDaAzar = salDaAzar();
		Question beberFazMal = beberFazMal();
		Question androidRuim = androidRuim();
		
		List<Question> perguntasComSal = questions.withTagVisible(sal);

		assertTrue(perguntasComSal.contains(salDaAzar));
		assertFalse(perguntasComSal.contains(beberFazMal));
		assertFalse(perguntasComSal.contains(androidRuim));
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

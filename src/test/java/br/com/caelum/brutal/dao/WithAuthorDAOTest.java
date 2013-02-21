package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

public class WithAuthorDAOTest extends DatabaseTestCase{

	private User author;
	private WithAuthorDAO<Question> withUser;

	@Before
	public void setup() {
		author = new User("Leonardo", "leo@leo", "123456");
		session.save(author);
		withUser = new WithAuthorDAO<Question>(session, Question.class);
	}
	
	@Test
	public void should_return_only_questions_with_the_provided_user_ordered_by_vote_count() {
		Question beberFazMal = question("Por que dizem que beber demais faz mal?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		beberFazMal.substitute(null, new Vote(author, VoteType.UP));
		beberFazMal.substitute(null, new Vote(author, VoteType.UP));
		
		Question androidRuim = question("Por que a api de android é tão ruim?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		androidRuim.substitute(null, new Vote(author, VoteType.UP));

		Question salDaAzar =  question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		
		session.save(salDaAzar);
		session.save(beberFazMal);
		session.save(androidRuim);
		
		List<Question> perguntasDoAuthor = withUser.byVotes(author);

		assertTrue(perguntasDoAuthor.contains(salDaAzar));
		assertTrue(perguntasDoAuthor.contains(beberFazMal));
		assertTrue(perguntasDoAuthor.contains(androidRuim));
		assertEquals(perguntasDoAuthor.get(0), beberFazMal);
		assertEquals(perguntasDoAuthor.get(1), androidRuim);
		assertEquals(perguntasDoAuthor.get(2), salDaAzar);
	}
	
	@Test
	public void should_return_only_questions_with_the_provided_user_ordered_by_date() {

		DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(2).getMillis());
		Question beberFazMal = question("Por que dizem que beber demais faz mal?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		DateTimeUtils.setCurrentMillisSystem();
		
		DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(1).getMillis());
		Question androidRuim = question("Por que a api de android é tão ruim?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		DateTimeUtils.setCurrentMillisSystem();

		Question salDaAzar =  question("Por que pegar o sal da mal dos outros da azar?", "Alguem poderia me dizer o por que disso? Obrigado galera!", author);
		
		session.save(salDaAzar);
		session.save(beberFazMal);
		session.save(androidRuim);
		
		List<Question> perguntasDoAuthor = withUser.byDate(author);
		
		assertTrue(perguntasDoAuthor.contains(salDaAzar));
		assertTrue(perguntasDoAuthor.contains(beberFazMal));
		assertTrue(perguntasDoAuthor.contains(androidRuim));
		assertEquals(salDaAzar, perguntasDoAuthor.get(0));
		assertEquals(androidRuim, perguntasDoAuthor.get(1));
		assertEquals(beberFazMal, perguntasDoAuthor.get(2));
	}
}

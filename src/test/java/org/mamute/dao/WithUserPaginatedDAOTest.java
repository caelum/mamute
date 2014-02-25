package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mamute.dao.WithUserPaginatedDAO.OrderType.ByDate;
import static org.mamute.dao.WithUserPaginatedDAO.OrderType.ByVotes;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.dao.WithUserPaginatedDAO;
import org.mamute.dao.WithUserPaginatedDAO.UserRole;
import org.mamute.model.Answer;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;

public class WithUserPaginatedDAOTest extends DatabaseTestCase{

	private User author = user("Leonardo", "leo@leo");
	private WithUserPaginatedDAO<Question> questionsWithUser;
	private WithUserPaginatedDAO<Answer> answersWithUser;
	private QuestionBuilder question = new QuestionBuilder();
	
	private Vote upVote = new Vote(author, VoteType.UP);
	private Vote upVote2 = new Vote(author, VoteType.UP);
	private Vote upVote3 = new Vote(author, VoteType.UP);
	private Tag defaultTag = tag("defaultTag");

	@Before
	public void setup() {
		session.save(author);
		session.save(upVote);
		session.save(upVote2);
		session.save(upVote3);
		session.save(defaultTag);
		InvisibleForUsersRule invisibleFilter = new InvisibleForUsersRule(new LoggedUser(author, null));
		questionsWithUser = new WithUserPaginatedDAO<Question>(session, Question.class, UserRole.AUTHOR, invisibleFilter);
		answersWithUser = new WithUserPaginatedDAO<Answer>(session, Answer.class, UserRole.AUTHOR, invisibleFilter);
	}
	
	@Test
	public void should_return_only_questions_with_the_provided_user_ordered_by_vote_count() {
		Question beberFazMal = beberFazMal();
		beberFazMal.substitute(null, upVote);
		beberFazMal.substitute(null, upVote2);
		
		Question androidRuim = androidRuim();
		androidRuim.substitute(null, upVote3);
		
		Question salDaAzar = salDaAzar();
		
		List<Question> perguntasDoAuthor = questionsWithUser.by(author, ByVotes, 1);

		assertTrue(perguntasDoAuthor.contains(salDaAzar));
		assertTrue(perguntasDoAuthor.contains(beberFazMal));
		assertTrue(perguntasDoAuthor.contains(androidRuim));
		assertEquals(perguntasDoAuthor.get(0), beberFazMal);
		assertEquals(perguntasDoAuthor.get(1), androidRuim);
		assertEquals(perguntasDoAuthor.get(2), salDaAzar);
	}
	
	@Test
	public void should_return_only_answers_with_the_provided_user_ordered_by_vote_count() {
		Question androidRuim = androidRuim();

		Answer ehMuito = answer("Sim! É muito ruim! (tanto quanto iOS)", androidRuim, author);
		ehMuito.substitute(null, upVote);
		ehMuito.substitute(null, upVote2);
		
		Answer nemEh = answer("Não, é a melhor plataforma mobile do mundo!", androidRuim, author);
		nemEh.substitute(null, upVote3);
		
		Answer maisOuMenos =  answer("Cara, veja bem: depende do seu ponto de vista.", androidRuim, author);
		
		session.save(upVote);
		session.save(upVote2);
		session.save(upVote3);
		session.save(maisOuMenos);
		session.save(ehMuito);
		session.save(nemEh);
		
		List<Answer> respostasDoAuthor = answersWithUser.by(author, ByVotes, 1);
		
		assertTrue(respostasDoAuthor.contains(maisOuMenos));
		assertTrue(respostasDoAuthor.contains(ehMuito));
		assertTrue(respostasDoAuthor.contains(nemEh));
		assertEquals(respostasDoAuthor.get(0), ehMuito);
		assertEquals(respostasDoAuthor.get(1), nemEh);
		assertEquals(respostasDoAuthor.get(2), maisOuMenos);
	}
	
	@Test
	public void should_return_only_questions_with_the_provided_user_ordered_by_date() {
		DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(2).getMillis());
		Question beberFazMal = beberFazMal();
		DateTimeUtils.setCurrentMillisSystem();
		
		DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(1).getMillis());
		Question androidRuim = androidRuim();
		DateTimeUtils.setCurrentMillisSystem();

		Question salDaAzar = salDaAzar();
		
		List<Question> perguntasDoAuthor = questionsWithUser.by(author, ByDate, 1);
		
		assertTrue(perguntasDoAuthor.contains(salDaAzar));
		assertTrue(perguntasDoAuthor.contains(beberFazMal));
		assertTrue(perguntasDoAuthor.contains(androidRuim));
		assertEquals(salDaAzar, perguntasDoAuthor.get(0));
		assertEquals(androidRuim, perguntasDoAuthor.get(1));
		assertEquals(beberFazMal, perguntasDoAuthor.get(2));
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
				.withTag(defaultTag)
				.build();
		session.save(salDaAzar);
		return salDaAzar;
	}

}
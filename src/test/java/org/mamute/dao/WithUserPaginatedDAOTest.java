package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mamute.dao.WithUserPaginatedDAO.OrderType.ByDate;
import static org.mamute.dao.WithUserPaginatedDAO.OrderType.ByVotes;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
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
		Question javaEEQuestion = javaEEQuestion();
		javaEEQuestion.substitute(null, upVote);
		javaEEQuestion.substitute(null, upVote2);
		
		Question androidQuestion = androidQuestion();
		androidQuestion.substitute(null, upVote3);
		
		Question javaQuestion = javaQuestion();
		
		List<Question> perguntasDoAuthor = questionsWithUser.by(author, ByVotes, 1);

		assertTrue(perguntasDoAuthor.contains(javaQuestion));
		assertTrue(perguntasDoAuthor.contains(javaEEQuestion));
		assertTrue(perguntasDoAuthor.contains(androidQuestion));
		assertEquals(perguntasDoAuthor.get(0), javaEEQuestion);
		assertEquals(perguntasDoAuthor.get(1), androidQuestion);
		assertEquals(perguntasDoAuthor.get(2), javaQuestion);
	}
	
	@Test
	public void should_return_only_answers_with_the_provided_user_ordered_by_vote_count() {
		Question androidQuestion = androidQuestion();

		Answer ehMuito = answer("Sim! É muito ruim! (tanto quanto iOS)", androidQuestion, author);
		ehMuito.substitute(null, upVote);
		ehMuito.substitute(null, upVote2);
		
		Answer nemEh = answer("Não, é a melhor plataforma mobile do mundo!", androidQuestion, author);
		nemEh.substitute(null, upVote3);
		
		Answer maisOuMenos =  answer("Cara, veja bem: depende do seu ponto de vista.", androidQuestion, author);
		
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
		Question javaEEQuestion = javaEEQuestion();
		DateTimeUtils.setCurrentMillisSystem();
		
		DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(1).getMillis());
		Question androidQuestion = androidQuestion();
		DateTimeUtils.setCurrentMillisSystem();

		Question javaQuestion = javaQuestion();
		
		List<Question> perguntasDoAuthor = questionsWithUser.by(author, ByDate, 1);
		
		assertTrue(perguntasDoAuthor.contains(javaQuestion));
		assertTrue(perguntasDoAuthor.contains(javaEEQuestion));
		assertTrue(perguntasDoAuthor.contains(androidQuestion));
		assertEquals(javaQuestion, perguntasDoAuthor.get(0));
		assertEquals(androidQuestion, perguntasDoAuthor.get(1));
		assertEquals(javaEEQuestion, perguntasDoAuthor.get(2));
	}

	private Question javaEEQuestion(){
		Question q = question
			.withTitle("Some question about java ee and other stuff?")
			.withDescription("Please help solving my question about java ee! Thanks, guys!")
			.withAuthor(author)
			.withTag(defaultTag)
			.build();
		session.save(q);
		return q;
	}
	
	private Question androidQuestion(){
		Question q = question
				.withTitle("Some question about android and other stuff?")
				.withDescription("Please help solving my question about android! Thanks, guys!")
				.withAuthor(author)
				.withTag(defaultTag)
				.build();
		session.save(q);
		return q;
		
	}

	private Question javaQuestion(){
		Question q =  question
				.withTitle("Some question about java SE and other stuff")
				.withDescription("Please help solving my question about java! Thanks, guys!")
				.withAuthor(author)
				.withTag(defaultTag)
				.build();
		session.save(q);
		return q;
	}

	@After
	public void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}
}
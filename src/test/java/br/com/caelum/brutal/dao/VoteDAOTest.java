package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

public class VoteDAOTest extends DatabaseTestCase{
	
    private Question question = new Question("Tiny title Tiny title Tiny title", "Description 1234567890123456789012345678901234567890");
    private User author = new User("nome", "email", "123");
    private User otherUser = new User("blabla", "blabla@gmail", "123");

    @Before
	public void before_testing() {
	    session.save(otherUser);
	    session.save(author);
	    session.save(question);
	}

	@Test
	public void should_delete_previous_and_save_new() {
		Vote current = new Vote(author, VoteType.UP);
		new VoteDAO(session).substitute(null, current);
		assertTrue(session.contains(current));
	}
	@Test
	public void should_ignore_previous_null_and_save_new() {
		Vote previous = new Vote(author, VoteType.UP);
		new VoteDAO(session).substitute(null, previous);
		assertTrue(session.contains(previous));

		Vote current = new Vote(author, VoteType.DOWN);
		new VoteDAO(session).substitute(previous, current);
		assertTrue(session.contains(current));
		assertFalse(session.contains(previous));
	}
	
	@Test
	public void should_verify_that_a_user_already_voted_a_question() {
		Vote previous = new Vote(author, VoteType.UP);
		new VoteDAO(session).substitute(null, previous);
		question.substitute(null,previous);
		session.save(question);
		
		Vote found = new VoteDAO(session).previousVoteFor(question.getId(), author, Question.class);
		assertEquals(previous, found);

		Vote current = new Vote(author, VoteType.DOWN);
		new VoteDAO(session).substitute(previous, current);
		question.substitute(previous,current);
		session.save(question);

		found = new VoteDAO(session).previousVoteFor(question.getId(), author, Question.class);
		assertEquals(current, found);
	}

}

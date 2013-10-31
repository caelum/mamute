package br.com.caelum.brutal.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;

public class RetrieveKarmaTest extends TestCase{

	private RetrieveKarma retrieveKarma;
	private Question question;
	private RetrieveKarma retrieveKarmaWithoutModerator;

	@Before
	public void setUp () {
		User moderator = user(null, null).asModerator();
		User user = user(null, null);
		retrieveKarma = new RetrieveKarma(new LoggedUser(moderator, null), null, null);
		retrieveKarmaWithoutModerator = new RetrieveKarma(new LoggedUser(user, null), null, null);
		question = question(null);
	}
	
	@Test
	public void should_handle_question_with_moderator() {
		assertTrue(retrieveKarma.shouldHandle(question));
	}
	
	@Test
	public void should_handle_answer_with_moderator() {
		Answer answer= answer(null, question, null);
		assertTrue(retrieveKarma.shouldHandle(answer));
	}
	
	@Test
	public void should_not_handle_question_without_moderator() {
		assertFalse(retrieveKarmaWithoutModerator.shouldHandle(question));
	}
	
	@Test
	public void should_not_handle_answer_without_moderator() {
		Answer answer= answer(null, question, null);
		assertFalse(retrieveKarmaWithoutModerator.shouldHandle(answer));
	}
	
	@Test
	public void should_not_handle_comment() {
		Comment comment = comment(null, null);
		assertFalse(retrieveKarma.shouldHandle(comment));
	}

}

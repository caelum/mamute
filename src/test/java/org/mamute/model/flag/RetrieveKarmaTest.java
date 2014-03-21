package org.mamute.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.flag.RetrieveKarma;

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

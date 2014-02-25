package org.mamute.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.flag.RemoveSolutionFlaggedByModerator;
import org.mamute.model.interfaces.Flaggable;

public class RemoveSolutionFlaggedByModeratorTest extends TestCase {
	private User moderator = moderator();
	private User user = user("name", "email@brutal");
	private LoggedUser loggedModerator = new LoggedUser(moderator, null);
	private LoggedUser loggedUser = new LoggedUser(user, null);

	@Test
	public void should_handle_answer_flagged_by_moderator() {
		RemoveSolutionFlaggedByModerator action = new RemoveSolutionFlaggedByModerator(loggedModerator);
		assertTrue(action.shouldHandle(new AnswerChild()));
		assertTrue(action.shouldHandle(answer("blabla", question(null), null)));
	}
	
	@Test
	public void should_not_handle_flag_of_normal_user() {
		RemoveSolutionFlaggedByModerator action = new RemoveSolutionFlaggedByModerator(loggedUser);
		Flaggable flaggable = new AnswerChild();
		assertFalse(action.shouldHandle(flaggable));
	}
	
	@Test
	public void should_remove_solution_from_question() throws Exception {
		RemoveSolutionFlaggedByModerator action = new RemoveSolutionFlaggedByModerator(loggedModerator);
		Question question = question(user);
		Answer answer = answer("blabla", question, user);
		answer.markAsSolution();
		action.fire(answer);
		
		assertFalse(answer.isSolution());
		assertFalse(question.isSolved());
	}
	
	/**
	 * to test if hibernate proxies will be handled
	 */
	@SuppressWarnings("deprecation")
	private class AnswerChild extends Answer {
	}

}

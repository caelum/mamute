package br.com.caelum.brutal.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Flaggable;

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

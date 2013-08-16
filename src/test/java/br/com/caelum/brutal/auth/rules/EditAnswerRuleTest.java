package br.com.caelum.brutal.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.brutauth.auth.rules.EditAnswerRule;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.LoggedUser;

public class EditAnswerRuleTest extends TestCase{
	private static final long NOT_AUTHOR = 2l;
	private static final Long AUTHOR_ID = 1l;
	private LoggedUser author;
	private Answer answer;

	@Before
	public void setUp(){
		author = loggedUser("author", "author@brutal.com", AUTHOR_ID);
		answer = answer(null, question(author.getCurrent()), author.getCurrent());
	}

	@Test
	public void author_should_be_allowed_to_edit_moderatable() {
		assertTrue(new EditAnswerRule(author).isAllowed(answer));
	}
	
	@Test
	public void loggedUser_with_enough_karma_should_be_allowed_to_edit() {
		LoggedUser loggedUserWithEnoughKarma = loggedUser("loggedUser", "loggedUser@brutal.com", NOT_AUTHOR);
		loggedUserWithEnoughKarma.getCurrent().increaseKarma(PermissionRulesConstants.EDIT_ANSWER);
		
		assertTrue(new EditAnswerRule(loggedUserWithEnoughKarma).isAllowed(answer));
	}
	
	@Test
	public void moderator_should_be_allowed_to_edit() {
		LoggedUser loggedUser = loggedUser("moderator", "moderator@brutal.com", NOT_AUTHOR);
		loggedUser.getCurrent().asModerator();
		assertTrue(new EditAnswerRule(loggedUser).isAllowed(answer));
	}
	
	@Test
	public void loggedUser_with_low_karma_should_not_be_allowed_to_edit() {
		LoggedUser other = loggedUser("other", "other@brutal.com", NOT_AUTHOR);
		assertFalse(new EditAnswerRule(other).isAllowed(answer));
	}

}

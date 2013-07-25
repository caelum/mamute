package br.com.caelum.brutal.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.brutauth.auth.rules.EditAnswerRule;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.User;

public class EditAnswerRuleTest extends TestCase{
	private User author;
	private Answer answer;

	@Before
	public void setUp(){
		author = user("author", "author@brutal.com", 1l);
		answer = answer(null, question(author), author);
	}

	@Test
	public void author_should_be_allowed_to_edit_moderatable() {
		assertTrue(new EditAnswerRule(author).isAllowed(answer));
	}
	
	@Test
	public void user_with_enough_karma_should_be_allowed_to_edit() {
		User userWithEnoughKarma = user("user", "user@brutal.com", 2l);
		userWithEnoughKarma.increaseKarma(PermissionRulesConstants.EDIT_ANSWER);
		
		assertTrue(new EditAnswerRule(userWithEnoughKarma).isAllowed(answer));
	}
	
	@Test
	public void moderator_should_be_allowed_to_edit() {
		User moderator = user("moderator", "moderator@brutal.com", 2l).asModerator();
		assertTrue(new EditAnswerRule(moderator).isAllowed(answer));
	}
	
	@Test
	public void user_with_low_karma_should_not_be_allowed_to_edit() {
		User other = user("other", "other@brutal.com", 3l);
		assertFalse(new EditAnswerRule(other).isAllowed(answer));
	}

}

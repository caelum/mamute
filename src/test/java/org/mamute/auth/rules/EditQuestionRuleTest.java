package org.mamute.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mamute.auth.rules.PermissionRulesConstants;
import org.mamute.brutauth.auth.rules.EditQuestionRule;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;

public class EditQuestionRuleTest extends TestCase {

	private Question question;
	private LoggedUser author;

	@Before
	public void setUp(){
		author = loggedUser("author", "author@brutal.com", 1l);
		question = new QuestionBuilder().withAuthor(author.getCurrent()).build();
	}

	@Test
	public void author_should_be_allowed_to_edit_moderatable() {
		assertTrue(new EditQuestionRule(author).isAllowed(question));
	}
	
	@Test
	public void user_with_enough_karma_should_be_allowed_to_edit() {
		LoggedUser userWithEnoughKarma = loggedUser("user", "user@brutal.com", 2l);
		userWithEnoughKarma.getCurrent().increaseKarma(PermissionRulesConstants.EDIT_QUESTION);
		
		assertTrue(new EditQuestionRule(userWithEnoughKarma).isAllowed(question));
	}
	
	@Test
	public void moderator_should_be_allowed_to_edit() {
		LoggedUser moderator = loggedUser("moderator", "moderator@brutal.com", 2l);
		moderator.getCurrent().asModerator();
		assertTrue(new EditQuestionRule(moderator).isAllowed(question));
	}
	
	@Test
	public void user_with_low_karma_should_not_be_allowed_to_edit() {
		LoggedUser other = loggedUser("other", "other@brutal.com", 3l);
		assertFalse(new EditQuestionRule(other).isAllowed(question));
	}

}

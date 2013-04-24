package br.com.caelum.brutal.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;

public class AuthorizationSystemTest extends TestCase {

	private User author;
	private Question question;
	private Answer answer;

	@Before
	public void setup() {
		author = user("author", "author@brutal.com", 1l);
		question = new QuestionBuilder().withAuthor(author).build();
		answer = answer("blabla", question, author);
	}
	
	@Test
	public void author_should_be_allowed_to_edit_moderatable() {
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(author);
		PermissionRule<Moderatable> ruleForQuestionEdit = authorizationSystem.ruleForQuestionEdit();
		PermissionRule<Moderatable> ruleForAnswerEdit = authorizationSystem.ruleForAnswerEdit();
		
		assertTrue(ruleForQuestionEdit.isAllowed(author, question));
		assertTrue(ruleForAnswerEdit.isAllowed(author, answer));
	}
	
	@Test
	public void user_with_enough_karma_should_be_allowed_to_edit() {
		User userWithEnoughKarma = user("user", "user@brutal.com", 2l);
		userWithEnoughKarma.increaseKarma(PermissionRulesConstants.EDIT_QUESTION);
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(userWithEnoughKarma);
		PermissionRule<Moderatable> ruleForQuestionEdit = authorizationSystem.ruleForQuestionEdit();
		
		assertTrue(ruleForQuestionEdit.isAllowed(userWithEnoughKarma, question));
	}
	
	@Test
	public void moderator_should_be_allowed_to_edit() {
		User moderator = user("moderator", "moderator@brutal.com", 2l).asModerator();
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(moderator);
		PermissionRule<Moderatable> ruleForQuestionEdit = authorizationSystem.ruleForQuestionEdit();
		
		assertTrue(ruleForQuestionEdit.isAllowed(moderator, question));
	}
	
	@Test
	public void user_with_low_karma_should_not_be_allowed_to_edit() {
		User other = user("other", "other@brutal.com", 3l);
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(other);
		PermissionRule<Moderatable> ruleForQuestionEdit = authorizationSystem.ruleForQuestionEdit();
		
		assertFalse(ruleForQuestionEdit.isAllowed(other, question));
	}


}

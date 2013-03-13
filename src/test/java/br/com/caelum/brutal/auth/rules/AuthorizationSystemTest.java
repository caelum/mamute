package br.com.caelum.brutal.auth.rules;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;

public class AuthorizationSystemTest extends TestCase {

	private User author;
	private Moderatable question;

	@Before
	public void setup() {
		author = user("author", "author@brutal.com", 1l);
		question = new QuestionBuilder().withAuthor(author).build();
	}
	
	@Test
	public void author_should_be_allowed_to_edit() {
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(author);
		assertTrue(authorizationSystem.canEdit(question, 100));
	}
	
	@Test
	public void user_with_enough_karma_should_be_allowed_to_edit() {
		User userWithEnoughKarma = user("user", "user@brutal.com", 2l);
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(userWithEnoughKarma);
		
		assertTrue(authorizationSystem.canEdit(question, 0));
	}
	
	@Test
	public void moderator_should_be_allowed_to_edit() {
		User moderator = user("moderator", "moderator@brutal.com", 2l).asModerator();
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(moderator);
		
		assertTrue(authorizationSystem.canEdit(question, 100));
	}
	
	@Test(expected=UnauthorizedException.class)
	public void user_with_low_karma_should_not_be_allowed_to_edit() {
		User other = user("other", "other@brutal.com", 3l);
		
		AuthorizationSystem authorizationSystem = new AuthorizationSystem(other );
		authorizationSystem.canEdit(question, 100);
	}


}

package br.com.caelum.brutal.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;

public class RemoveFlaggedByModeratorTest extends TestCase {
	private User user = user("moderator", "email@brutal.com").asModerator();
	private User author = user("author", "author@brutal.com");

	@Test
	public void should_remove_comment_after_moderator_flag() {
		RemoveFlaggedByModerator removeFlaggedByModerator = new RemoveFlaggedByModerator(new LoggedUser(user, null));
		Comment comment = comment(author, "blablablablba");
		removeFlaggedByModerator.fire(comment);
		
		assertTrue(comment.isInvisible());
	}
	
	@Test
	public void should_not_remove_after_normal_user_flag() throws Exception {
		User normal = user("normal", "normal@brutal.com");
		RemoveFlaggedByModerator removeFlaggedByModerator = new RemoveFlaggedByModerator(new LoggedUser(normal, null));
		Comment comment = comment(author, "blablablablba");
		removeFlaggedByModerator.fire(comment);
		
		assertFalse(comment.isInvisible());
	}
	
	@Test
	public void should_not_remove_after_normal_not_logged_user() throws Exception {
		RemoveFlaggedByModerator removeFlaggedByModerator = new RemoveFlaggedByModerator(new LoggedUser(null, null));
		Comment comment = comment(author, "blablablablba");
		removeFlaggedByModerator.fire(comment);
		
		assertFalse(comment.isInvisible());
	}

}

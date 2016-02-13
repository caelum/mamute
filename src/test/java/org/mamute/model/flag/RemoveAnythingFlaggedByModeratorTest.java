package org.mamute.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.Comment;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;
import org.mamute.model.flag.RemoveAnythingFlaggedByModerator;

public class RemoveAnythingFlaggedByModeratorTest extends TestCase {
	private User user = user("moderator", "email@brutal.com").asModerator();
	private User author = user("author", "author@brutal.com");

	@Test
	public void should_remove_comment_after_moderator_flag() {
		RemoveAnythingFlaggedByModerator removeFlaggedByModerator = new RemoveAnythingFlaggedByModerator(new LoggedUser(user, null));
		Comment comment = comment(author, "blablablablba");
		removeFlaggedByModerator.fire(comment);
		
		assertFalse(comment.isVisible());
	}
	
	@Test
	public void should_not_handle_normal_user() throws Exception {
		User normal = user("normal", "normal@brutal.com");
		RemoveAnythingFlaggedByModerator removeFlaggedByModerator = new RemoveAnythingFlaggedByModerator(new LoggedUser(normal, null));
		Comment comment = comment(author, "blablablablba");
		
		assertFalse(removeFlaggedByModerator.shouldHandle(comment));
		assertTrue(comment.isVisible());
	}
	
	@Test
	public void should_not_handle_not_logged_user() throws Exception {
		RemoveAnythingFlaggedByModerator removeFlaggedByModerator = new RemoveAnythingFlaggedByModerator(new LoggedUser(null, null));
		Comment comment = comment(author, "blablablablba");
		
		assertFalse(removeFlaggedByModerator.shouldHandle(comment));
		assertTrue(comment.isVisible());
	}

}

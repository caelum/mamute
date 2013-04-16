package br.com.caelum.brutal.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;

public class RemoveAnythingFlaggedByModeratorTest extends TestCase {
	private User user = user("moderator", "email@brutal.com").asModerator();
	private User author = user("author", "author@brutal.com");

	@Test
	public void should_remove_comment_after_moderator_flag() {
		RemoveAnythingFlaggedByModerator removeFlaggedByModerator = new RemoveAnythingFlaggedByModerator(new LoggedUser(user, null));
		Comment comment = comment(author, "blablablablba");
		removeFlaggedByModerator.fire(comment);
		
		assertTrue(comment.isInvisible());
	}
	
	@Test
	public void should_not_handle_normal_user() throws Exception {
		User normal = user("normal", "normal@brutal.com");
		RemoveAnythingFlaggedByModerator removeFlaggedByModerator = new RemoveAnythingFlaggedByModerator(new LoggedUser(normal, null));
		Comment comment = comment(author, "blablablablba");
		
		assertFalse(removeFlaggedByModerator.shouldHandle(comment));
		assertFalse(comment.isInvisible());
	}
	
	@Test
	public void should_not_handle_not_logged_user() throws Exception {
		RemoveAnythingFlaggedByModerator removeFlaggedByModerator = new RemoveAnythingFlaggedByModerator(new LoggedUser(null, null));
		Comment comment = comment(author, "blablablablba");
		
		assertFalse(removeFlaggedByModerator.shouldHandle(comment));
		assertFalse(comment.isInvisible());
	}

}

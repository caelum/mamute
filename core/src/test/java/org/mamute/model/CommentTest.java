package org.mamute.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.Comment;
import org.mamute.model.Flag;
import org.mamute.model.FlagType;
import org.mamute.model.User;

public class CommentTest extends TestCase {

	@Test
	public void should_verify_that_user_flagged_a_comment() {
		User author = user("name", "email@email", 1l);
		User other = user("other", "other@brutal.com", 2l);
		User commentAuthor = user("name", "email@email", 1l);
		Comment comment = comment(commentAuthor, "comment");
		
		Flag flag = new Flag(FlagType.RUDE, author);
		comment.add(flag);
		
		assertTrue(comment.alreadyFlaggedBy(author));
		assertFalse(comment.alreadyFlaggedBy(other));
	}

}

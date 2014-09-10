package org.mamute.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.MarkedText.notMarked;

import org.junit.Test;
import org.mamute.model.Comment;
import org.mamute.model.Flag;
import org.mamute.model.FlagType;
import org.mamute.model.User;

public class FlagDaoTest extends DatabaseTestCase {
	
	@Test
	public void should_verify_that_user_flagged_comment() {
		FlagDao flags = new FlagDao(session);
		User author = user("user user", "chico@brutal.com");
		User other = user("user user", "other@brutal.com");
		session.save(author);
		session.save(other);

		Comment commentFromAuthor = createCommentWithFlag(author);
		Comment commentFromOther = createCommentWithFlag(other);
		
		assertFalse(flags.alreadyFlagged(author, commentFromOther.getId(), Comment.class));
		assertTrue(flags.alreadyFlagged(author, commentFromAuthor.getId(), Comment.class));
	}
	
	

	private Comment createCommentWithFlag(User author) {
		Comment comment = new Comment(author, notMarked("my comment my comment my comment"));
		Flag flag = new Flag(FlagType.RUDE, author);
		comment.add(flag);
		session.save(flag);
		session.save(comment);
		return comment;
	}

}

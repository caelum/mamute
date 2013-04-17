package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.dto.FlaggableAndFlagCount;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.User;

public class FlaggableDAOTest extends DatabaseTestCase {

	@Test
	public void should_get_visible_comments_with_three_flags_() throws Exception {
		FlaggableDAO flaggables = new FlaggableDAO(session);
    	User author = user("author author", "author@brutal.com");
		Comment comment = comment(author, "comment comment comment comment");
		Comment other = comment(author, "comment comment comment comment");
		Comment flaggedInvisible = comment(author, "invisible invisible invisible invisible");
		flaggedInvisible.remove();
		Flag flag4 = flag(FlagType.RUDE, author);
		
		addFlags(comment, 3, author);
		addFlags(flaggedInvisible, 3, author);
		other.add(flag4);
		
		session.save(author);
		session.save(comment);
		session.save(flaggedInvisible);
		session.save(other);
		session.save(flag4);
		
		List<FlaggableAndFlagCount> flagged = flaggables.flaggedButVisible(Comment.class, 3l);
		assertEquals(1, flagged.size());
		assertEquals(3l, flagged.get(0).getFlagCount());
		
	}

	private void addFlags(Comment comment, int n, User author) {
		for (int i = 0; i < n; i++) {
			Flag flag = flag(FlagType.RUDE, author);
			session.save(flag);
			comment.add(flag);
		}
	}


}

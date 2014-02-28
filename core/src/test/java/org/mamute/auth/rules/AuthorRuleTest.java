package org.mamute.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mamute.auth.rules.AuthorRule;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.interfaces.Moderatable;

public class AuthorRuleTest extends TestCase {

	@Test
	public void should_allow_moderatable_author() {
		AuthorRule<Moderatable> rule = new AuthorRule<>();
		User author = user("author", "author@brutal.com", 1l);
		User other = user("other", "other@brutal.com", 2l);
		Question question = new QuestionBuilder().withAuthor(author).build();
		
		assertFalse(rule.isAllowed(other, question));
		assertTrue(rule.isAllowed(author, question));
	}

}

package br.com.caelum.brutal.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;

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

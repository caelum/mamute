package br.com.caelum.brutal.builders;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.pagpag.integracao.dao.DatabaseTestCase;

public class TagBuilderTest extends DatabaseTestCase{

	@Test
	public void shouldBuildATag() {
		TagDAO tagDAO = new TagDAO(session);
		QuestionDAO questionDAO = new QuestionDAO(session);
		UserDAO userDAO = new UserDAO(session);
		TagBuilder tagBuilder = new TagBuilder(tagDAO, userDAO, questionDAO);
		
		Question whyToUseRails = new Question("Why to use rails?", "Why would I use rails?");
		User leonardo = new User("Leonardo Wolter", "leo@leo.com", "123");
		
		String railsTagName = "Rails";
		String railsTagDescription = "For questions with rails";
		Tag railsTag = tagBuilder.withName(railsTagName)
				  .withDescription(railsTagDescription)
				  .withQuestion(whyToUseRails)
				  .withAuthor(leonardo)
				  .notPersisted();
		
		assertEquals(railsTagName, railsTag.getName());
		assertEquals(railsTagDescription, railsTag.getDescription());
	}

}

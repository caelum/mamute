package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionInformationBuilder;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.TagUsage;
import br.com.caelum.brutal.model.User;


public class TagDAOTest extends DatabaseTestCase{

	private TagDAO tags;


	@Before
	public void setup() {
		this.tags = new TagDAO(session);
	}
	
	@Test
	public void should_save_if_does_not_exists_on_database() {
		Tag tag = new Tag("rails", "", null);
		Tag persistedTag = tags.saveOrLoad(tag);
		assertEquals(tag, persistedTag);
	}
	
	@Test
	public void should_load_if_exists_on_database() {
		String savedTagDescription = "for rails related questions";
		Tag rails = new Tag("rails", savedTagDescription, null);
		tags.saveOrLoad(rails);
		Tag otherRailsTag = new Tag("rails", "", null);
		Tag persistedTag = tags.saveOrLoad(otherRailsTag);
		assertEquals(rails, persistedTag);
		assertEquals(savedTagDescription, persistedTag.getDescription());
	}

	@Test
	public void should_load_recent_tags_used() throws Exception {
		User author = new User("autor", "x@x", "123");
		Tag tag = new Tag("java", "blablabla", author);
		Tag otherTag = new Tag("outraTag", "blablabla", author);
		
		QuestionInformation questionInformation = new QuestionInformationBuilder()
			.withAuthor(author).withTag(tag).build();
		QuestionInformation questionInformation2 = new QuestionInformationBuilder()
			.withAuthor(author).withTag(tag).build();
		QuestionInformation questionInformation3 = new QuestionInformationBuilder()
			.withAuthor(author).withTag(tag).build();
		QuestionInformation questionInformation4 = new QuestionInformationBuilder()
			.withAuthor(author).withTag(otherTag).build();
		
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(3).getMillis());
        Question question1 = new Question(questionInformation, author);
        DateTimeUtils.setCurrentMillisSystem();
        
		Question question2 = new Question(questionInformation2, author);
		Question question3 = new Question(questionInformation3, author);
		Question question4 = new Question(questionInformation4, author);
		
		session.save(author);
		session.save(tag);
		session.save(otherTag);
		session.save(questionInformation);
		session.save(questionInformation2);
		session.save(questionInformation3);
		session.save(questionInformation4);
		session.save(question1);
		session.save(question2);
		session.save(question3);
		session.save(question4);
		
		List<TagUsage> recentTagsUsage = tags.getRecentTagsUsageSince(new DateTime().minusMonths(2));
		
		assertEquals(2, recentTagsUsage.size());
		assertEquals(2l, recentTagsUsage.get(0).getUsage().longValue());
		assertEquals(1l, recentTagsUsage.get(1).getUsage().longValue());
		assertEquals(tag.getId(), recentTagsUsage.get(0).getTag().getId());
		
	}
	
}


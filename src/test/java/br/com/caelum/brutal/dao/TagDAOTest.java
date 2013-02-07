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
	private User leo;
	private Tag java;
	private Tag ruby;


	@Before
	public void setup() {
		this.tags = new TagDAO(session);
		leo = new User("leo", "leo@leo", "123");
		java = new Tag("java", "", leo);
		ruby = new Tag("ruby", "", leo);
		session.save(leo);
		session.save(java);
		session.save(ruby);
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
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(3).getMillis());
        questionWith(java);
        DateTimeUtils.setCurrentMillisSystem();
        
        questionWith(java);
        questionWith(java);
        questionWith(ruby);

		List<TagUsage> recentTagsUsage = tags.getRecentTagsUsageSince(new DateTime().minusMonths(2));
		
		assertEquals(2, recentTagsUsage.size());
		assertEquals(2l, recentTagsUsage.get(0).getUsage().longValue());
		assertEquals(1l, recentTagsUsage.get(1).getUsage().longValue());
		assertEquals(java.getId(), recentTagsUsage.get(0).getTag().getId());
		assertEquals(ruby.getId(), recentTagsUsage.get(1).getTag().getId());
		
	}
	
	@Test
	public void should_load_tags_with_usage_with_provided_name() throws Exception {
		questionWith(java);
		questionWith(java);
		questionWith(java);
		questionWith(ruby);
		List<TagUsage> tagsUsageLike = tags.findTagsUsageLike("ja");
		
		assertEquals(1, tagsUsageLike.size());
		assertEquals(3l, tagsUsageLike.get(0).getUsage().longValue());
		assertEquals(java.getId(), tagsUsageLike.get(0).getTag().getId());
	}

	private Question questionWith(Tag tag) {
		QuestionInformation questionInfo = new QuestionInformationBuilder()
			.withAuthor(leo).withTag(tag).build();
		Question question = new Question(questionInfo, leo);
		session.save(question);
		return question;
	}
	
}


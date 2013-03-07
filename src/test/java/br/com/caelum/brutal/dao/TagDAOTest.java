package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
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
		leo = user("leonardo", "leo@leo");
		java = new Tag("java", "", leo);
		ruby = new Tag("ruby", "", leo);
		session.save(leo);
		session.save(java);
		session.save(ruby);
	}

	@Test
	public void should_load_recent_tags_used() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(3).getMillis());
        questionWith(Arrays.asList(java));
        DateTimeUtils.setCurrentMillisSystem();
        
        questionWith(Arrays.asList(java));
        questionWith(Arrays.asList(java));
        questionWith(Arrays.asList(ruby));

		List<TagUsage> recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(2));
		
		assertEquals(2, recentTagsUsage.size());
		assertEquals(2l, recentTagsUsage.get(0).getUsage().longValue());
		assertEquals(1l, recentTagsUsage.get(1).getUsage().longValue());
		assertEquals(java.getId(), recentTagsUsage.get(0).getTag().getId());
		assertEquals(ruby.getId(), recentTagsUsage.get(1).getTag().getId());
		
	}
	
	@Test
	public void should_load_tags_with_usage_with_provided_name() throws Exception {
		questionWith(Arrays.asList(java));
		questionWith(Arrays.asList(java));
		questionWith(Arrays.asList(java));
		questionWith(Arrays.asList(ruby));
		List<Tag> tagsLike = tags.findTagsLike("ja");
		
		assertEquals(1, tagsLike.size());
 		assertEquals(3l, tagsLike.get(0).getUsageCount().longValue());
		assertEquals(java.getId(), tagsLike.get(0).getId());
	}
	
	@Test
	public void should_get_main_tags_of_the_provided_user() throws Exception {
		Question javaQuestion = questionWith(Arrays.asList(java));
		Answer javaAnswer = answer("just do this and that and you will be happy forever", javaQuestion, leo);
		session.save(javaAnswer);
		
		Question oneMoreJavaQuestion = questionWith(Arrays.asList(java));
		Answer oneMoreJavaAnswer = answer("just do this and that and you will be happy forever", oneMoreJavaQuestion, leo);
		session.save(oneMoreJavaAnswer);
		
		Question rubyQuestion = questionWith(Arrays.asList(ruby));
		Answer rubyAnswer = answer("just do this and that and you will be happy forever", rubyQuestion, leo);
		session.save(rubyAnswer);
		
		User chico = user("chicoh", "chico@chico.com");
		session.save(chico);
		Question otherJavaQuestion = questionWith(Arrays.asList(java));
		Answer otherJavaAnswer = answer("just do this and that and you will be happy forever", otherJavaQuestion, chico);
		session.save(otherJavaAnswer);
		
		
		List<TagUsage> mainTags = tags.findMainTagsOfUser(leo);
		
		for (TagUsage tagUsage : mainTags) {
			System.out.println(tagUsage.getTag().getName()+ " - "+ tagUsage.getUsage());
		}
	
		assertEquals(2, mainTags.size());
		assertEquals(2l, mainTags.get(0).getUsage().longValue());
		assertEquals(1l, mainTags.get(1).getUsage().longValue());
	}
	
	@Test
	public void should_load_all_tags_with_the_name_in_the_provided_string() throws Exception {
		List<Tag> tagsByNames = tags.findAllByNames("java ruby doesntexist");
		
		assertEquals(3, tagsByNames.size());
		assertEquals(java.getId(), tagsByNames.get(0).getId());
		assertEquals(ruby.getId(), tagsByNames.get(1).getId());
		assertNull(tagsByNames.get(2));
	}
	
	@Test
	public void should_get_all_tag_names() throws Exception {
		List<String> tagsNames = tags.allNames();
		
		assertEquals(2, tagsNames.size());
		assertEquals(java.getName(), tagsNames.get(0));
		assertEquals(ruby.getName(), tagsNames.get(1));
	}

	private Question questionWith(List<Tag> tags) {
		Question question = new QuestionBuilder().withAuthor(leo).withTags(tags).build();
		session.save(question);
		return question;
	}

	
}


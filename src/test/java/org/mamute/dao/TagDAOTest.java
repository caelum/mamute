package org.mamute.dao;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.Answer;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.TagUsage;
import org.mamute.model.User;


public class TagDAOTest extends DatabaseTestCase{

	private TagDAO tags;
	private User leo;
	private Tag java;
	private Tag ruby;
    private Tag weirdCharacters;

	@Before
	public void setup() {
		this.tags = new TagDAO(session);
		leo = user("leonardo", "leo@leo");
		java = new Tag("java", "", leo);
		ruby = new Tag("ruby", "", leo);
        weirdCharacters = new Tag("Čemaž {!weird!}#", "", leo);
		session.save(leo);
		session.save(java);
		session.save(ruby);
        session.save(weirdCharacters);
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
		
		assertEquals(2, mainTags.size());
		assertEquals(2l, mainTags.get(0).getUsage().longValue());
		assertEquals(1l, mainTags.get(1).getUsage().longValue());
	}
	
	@Test
	public void should_get_all_tag_names() throws Exception {
		List<String> tagsNames = tags.allNames();
		assertEquals(3, tagsNames.size());
        assertEquals(weirdCharacters.getName(), tagsNames.get(0));
        assertEquals(java.getName(), tagsNames.get(1));
        assertEquals(ruby.getName(), tagsNames.get(2));

    }
	
	@Test
	public void should_get_empty_list_for_nonexistant_names() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("blabla", "lala"));
		assertTrue(found.isEmpty());
	}

    @Test
    public void should_get_existing_tags_with_different_name_same_slug() throws Exception {
        List<Tag> found = tags.findAllDistinct(asList("cemaz weird"));
        assertFalse(found.isEmpty());
    }
	
	@Test
	public void should_split_two_spaces() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("java", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_find_tags_ordered() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("ruby", "java"));
		assertEquals(2, found.size());
		assertEquals("ruby", found.get(0).getName());
		assertEquals("java", found.get(1).getName());
	}
	
	@Test
	public void should_not_repeat_tags() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("java", "java", "ruby", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}

    @Test
	public void should_not_repeat_tags_even_if_different_case() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("Java", "java", "ruBy", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_not_save_tags_with_same_name() throws Exception {
		int originalSize = tags.all().size();
		Tag csharp = new Tag("csharp", "", leo);
		tags.saveIfDoesntExists(csharp);
		tags.saveIfDoesntExists(csharp);

		assertEquals(tags.all().size(), originalSize+1);
	}

    @Test
    public void should_not_save_tags_with_unique_name_but_same_slug() throws Exception {
        int originalSize = tags.all().size();
        Tag cevapciciWithSpecialChar = new Tag("čevapčiči", "", leo);
        Tag cevapciciWithoutSpecialChar = new Tag("cevapcici", "", leo);
        tags.saveIfDoesntExists(cevapciciWithSpecialChar);
        tags.saveIfDoesntExists(cevapciciWithoutSpecialChar);

        assertEquals(tags.all().size(), originalSize+1);
    }

    @Test
    public void should_define_correct_slugged_url()
    {
        assertNotNull(tags.findBySluggedName("java"));
        assertNotNull(tags.findBySluggedName("ruby"));
        assertNotNull(tags.findBySluggedName("cemaz-weird"));
    }

	private Question questionWith(List<Tag> tags) {
		Question question = new QuestionBuilder().withAuthor(leo).withTags(tags).build();
		session.save(question);
		return question;
	}

	
}


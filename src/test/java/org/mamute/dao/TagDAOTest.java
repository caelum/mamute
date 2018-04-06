package org.mamute.dao;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.*;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TagDAOTest extends DatabaseTestCase{

	@After
	public void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}

	@Test
	public void should_load_recent_tags_used() {
		User leo = createUser(session, "leonardo", "leo@leo");

		TagDAO tags = new TagDAO(session);
		Tag[] createdTags = createTags(session, leo);
		Tag java = createdTags[0];
		Tag ruby = createdTags[1];
		Tag scala = createdTags[2];

        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(3).getMillis());
        questionWith(Arrays.asList(java), leo);
        DateTimeUtils.setCurrentMillisSystem();
        questionWith(Arrays.asList(java), leo);
		questionWith(Arrays.asList(java), leo);
		questionWith(Arrays.asList(ruby), leo);
		questionWith(Arrays.asList(scala), leo);

		List<TagUsage> recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(2), 2);
		
		assertEquals(2, recentTagsUsage.size());
		assertEquals(2L, recentTagsUsage.get(0).getUsage().longValue());
		assertEquals(1L, recentTagsUsage.get(1).getUsage().longValue());
		assertEquals("java tag should be the most used", java.getId(), recentTagsUsage.get(0).getTag().getId());
		assertEquals("ruby tag should be the second-most used", ruby.getId(), recentTagsUsage.get(1).getTag().getId());
	}
	
	@Test
	public void should_get_main_tags_of_the_provided_user() {
		User leo = createUser(session, "leonardo", "leo@leo");

		TagDAO tags = new TagDAO(session);
		Tag[] createdTags = createTags(session, leo);
		Tag java = createdTags[0];
		Tag ruby = createdTags[1];

		Question javaQuestion = questionWith(Arrays.asList(java), leo);
		Answer javaAnswer = answer("just do this and that and you will be happy forever", javaQuestion, leo);
		session.save(javaAnswer);
		
		Question oneMoreJavaQuestion = questionWith(Arrays.asList(java), leo);
		Answer oneMoreJavaAnswer = answer("just do this and that and you will be happy forever", oneMoreJavaQuestion, leo);
		session.save(oneMoreJavaAnswer);
		
		Question rubyQuestion = questionWith(Arrays.asList(ruby), leo);
		Answer rubyAnswer = answer("just do this and that and you will be happy forever", rubyQuestion, leo);
		session.save(rubyAnswer);
		
		User chico = user("chicoh", "chico@chico.com");
		session.save(chico);
		Question otherJavaQuestion = questionWith(Arrays.asList(java), leo);
		Answer otherJavaAnswer = answer("just do this and that and you will be happy forever", otherJavaQuestion, chico);
		session.save(otherJavaAnswer);
		
		List<TagUsage> mainTags = tags.findMainTagsOfUser(leo);
		
		assertEquals(2, mainTags.size());
		assertEquals(2L, mainTags.get(0).getUsage().longValue());
		assertEquals(1L, mainTags.get(1).getUsage().longValue());
	}
	
	@Test
	public void should_get_all_tag_names() {
		TagDAO tags = new TagDAO(session);
		createTags(session, createUser(session, "leonardo", "leo@leo"));

		List<String> tagsNames = tags.allNames();
		assertEquals(4, tagsNames.size());
		assertTrue(tagsNames.containsAll(Arrays.asList("java", "groovy", "ruby", "scala")));
	}
	
	@Test
	public void should_get_empty_list_for_nonexistant_names() {
		TagDAO tags = new TagDAO(session);
		createTags(session, createUser(session, "leonardo", "leo@leo"));

		List<Tag> found = tags.findAllDistinct(asList("blabla", "lala"));
		assertTrue(found.isEmpty());
	}
	
	@Test
	public void should_split_two_spaces() {
		TagDAO tags = new TagDAO(session);
		createTags(session, createUser(session, "leonardo", "leo@leo"));

		List<Tag> found = tags.findAllDistinct(asList("java", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_find_tags_ordered() {
		TagDAO tags = new TagDAO(session);
		createTags(session, createUser(session, "leonardo", "leo@leo"));

		List<Tag> found = tags.findAllDistinct(asList("ruby", "java"));
		assertEquals(2, found.size());
		assertEquals("ruby", found.get(0).getName());
		assertEquals("java", found.get(1).getName());
	}
	
	@Test
	public void should_not_repeat_tags() {
		TagDAO tags = new TagDAO(session);
		createTags(session, createUser(session, "leonardo", "leo@leo"));

		List<Tag> found = tags.findAllDistinct(asList("java", "java", "ruby", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_not_repeat_tags_even_if_different_case() {
		TagDAO tags = new TagDAO(session);
		createTags(session, createUser(session, "leonardo", "leo@leo"));

		List<Tag> found = tags.findAllDistinct(asList("Java", "java", "ruBy", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_not_save_repeated_tags() {
		TagDAO tags = new TagDAO(session);
		User leo = createUser(session, "leonardo", "leo@leo");
		createTags(session, leo);

		int originalSize = tags.all().size();
		Tag csharp = new Tag("csharp", "", leo);
		tags.saveIfDoesntExists(csharp);
		tags.saveIfDoesntExists(csharp);

		assertEquals(tags.all().size(), originalSize+1);
	}

	private Question questionWith(List<Tag> tags, User leo) {
		Question question = new QuestionBuilder().withAuthor(leo).withTags(tags).build();
		session.save(question);
		return question;
	}

	private User createUser(Session session, String name, String email) {
		User leo = user(name, email);
		session.save(leo);
		return leo;
	}

	private Tag tag(String name, String description, User author, Session session) {
		Tag tag = new Tag(name, description, author);
		session.save(tag);
		return tag;
	}

	private Tag[] createTags(Session session, User leo) {
		return new Tag[] {
				tag("java", "", leo, session),
				tag("ruby", "", leo, session),
				tag("scala", "", leo, session),
				tag("groovy", "", leo, session)
		};
	}
}


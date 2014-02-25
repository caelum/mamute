package org.mamute.model;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.DatabaseTestCase;
import org.mamute.model.Comment;
import org.mamute.model.User;
import org.mamute.model.VisibleCommentList;

public class VisibleCommentListTest extends DatabaseTestCase{
	
	private VisibleCommentList commentList;
	private User leo;
	private List<Comment> invisibleComments;
	private User moderatorLeo;
	private User author;

	@Before
	public void setup() {
		commentList = new VisibleCommentList();
		author = user("leonardo", "leo@leo.com");
		leo = user("leonardo", "leo@leo.com");
		moderatorLeo = user("moderator", "moderator@leo.com").asModerator();
		Comment comment = comment(author, "meu teste de visibilidade de comentarios :)");
		comment.remove();
		invisibleComments = asList(comment);
		session.save(leo);
		session.save(moderatorLeo);
		session.save(author);
		session.save(comment);
	}
	
	@Test
	public void should_filter_comments_if_user_is_not_moderator_nor_author() {
		List<Comment> visibleComments = commentList.getVisibleCommentsFor(leo, invisibleComments);
		assertTrue(visibleComments.isEmpty());
	}
	
	@Test
	public void should_not_filter_if_user_is_moderator() {
		List<Comment> visibleComments = commentList.getVisibleCommentsFor(moderatorLeo, invisibleComments);
		assertFalse(visibleComments.isEmpty());
	}

	@Test
	public void should_not_filter_if_user_is_author() {
		List<Comment> visibleComments = commentList.getVisibleCommentsFor(author, invisibleComments);
		assertFalse(visibleComments.isEmpty());
	}
	
	@Test
	public void should_filter_if_user_is_null() {
		List<Comment> visibleComments = commentList.getVisibleCommentsFor(null, invisibleComments);
		assertTrue(visibleComments.isEmpty());
	}
}

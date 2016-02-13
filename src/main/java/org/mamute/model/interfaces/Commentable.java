package org.mamute.model.interfaces;

import java.io.Serializable;
import java.util.List;

import org.mamute.model.Comment;
import org.mamute.model.User;


public interface Commentable {

	Comment add(Comment comment);
	Serializable getId();
	List<Comment> getVisibleCommentsFor(User user);
	Watchable getMainThread();

}

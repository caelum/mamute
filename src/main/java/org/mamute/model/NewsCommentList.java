package org.mamute.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Embeddable
public class NewsCommentList extends VisibleCommentList{

	@JoinTable(name = "News_Comments")
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Comment> comments = new ArrayList<>();

	public void add(Comment comment) {
		comments.add(comment);
	}

	public List<Comment> getVisibleCommentsFor(User user) {
		return getVisibleCommentsFor(user, comments);
	}

	public void delete(Comment comment) {
		comments.remove(comment);
	}
}

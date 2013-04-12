package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Embeddable
public class AnswerCommentList extends VisibleCommentList{
	@JoinTable(name = "Answer_Comments")
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Comment> comments = new ArrayList<>();

	public void add(Comment comment) {
		comments.add(comment);
	}

	public List<Comment> getVisibleCommentsFor(User user) {
		return getVisibleCommentsFor(user, comments);
	}

}

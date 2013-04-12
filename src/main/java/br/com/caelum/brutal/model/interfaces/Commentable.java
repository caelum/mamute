package br.com.caelum.brutal.model.interfaces;

import java.io.Serializable;
import java.util.List;

import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.User;


public interface Commentable {

	Comment add(Comment comment);
	Serializable getId();
	List<Comment> getVisibleCommentsFor(User user);

}

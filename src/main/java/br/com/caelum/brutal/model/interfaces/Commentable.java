package br.com.caelum.brutal.model.interfaces;

import java.io.Serializable;
import java.util.List;

import br.com.caelum.brutal.model.Comment;


public interface Commentable {

	Comment add(Comment comment);
	Serializable getId();
	List<Comment> getComments();

}

package br.com.caelum.brutal.model;

import java.io.Serializable;
import java.util.List;

public interface Commentable {

	Comment add(Comment comment);
	Serializable getId();
	List<Comment> getComments();

}

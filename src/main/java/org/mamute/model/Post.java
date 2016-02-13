package org.mamute.model;

import org.mamute.model.interfaces.Commentable;
import org.mamute.model.interfaces.Flaggable;
import org.mamute.model.interfaces.Touchable;
import org.mamute.model.interfaces.Votable;

public interface Post extends Votable, Commentable, Touchable, Flaggable {
	
	String getTypeNameKey();

	void deleteComment(Comment comment);
}
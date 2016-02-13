package org.mamute.model;

import java.util.ArrayList;
import java.util.List;

public class VisibleCommentList {
	
	List<Comment> getVisibleCommentsFor(User user, List<Comment> comments){
		ArrayList<Comment> visibleComments = new ArrayList<Comment>();
		for (Comment comment : comments) {
			boolean isLogged = user != null;
			boolean userIsModeratorOrAuthor = isLogged && (user.isModerator() || user.isAuthorOf(comment));
			boolean isVisible = comment.isVisible() || userIsModeratorOrAuthor;
			if (isVisible && !comment.isDeleted()) {
				visibleComments.add(comment);
			}
		}
		return visibleComments;
	}
	
}

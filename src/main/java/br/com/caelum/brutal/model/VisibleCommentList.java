package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;

public class VisibleCommentList {
	
	List<Comment> getVisibleCommentsFor(User user, List<Comment> comments){
		ArrayList<Comment> visibleComments = new ArrayList<Comment>();
		for (Comment comment : comments) {
			boolean userIsModeratorOrAuthor = user != null && (user.isModerator() || user.isAuthorOf(comment));
			boolean isVisible = !comment.isInvisible() || userIsModeratorOrAuthor;
			if(isVisible){
				visibleComments.add(comment);
			}
		}
		return visibleComments;
	}
	
}

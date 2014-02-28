package org.mamute.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CommentsAndVotes {
	
	private final Map<Comment, Vote> votes;
	
	public CommentsAndVotes(List<Object[]> votes) {
		
		CommentComparator comparator = new CommentComparator();
		this.votes = new TreeMap<>(comparator);
		
		for (Object[] objects : votes) {
			Comment comment = (Comment) objects[0];
			Vote vote = (Vote) objects[1];
			this.votes.put(comment, vote);
		}
	}
	
	public Vote getVotes(Comment comment) {
		return this.votes.get(comment);
	}

}

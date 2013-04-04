package br.com.caelum.brutal.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CommentAndVotes {
	
	private final Map<Comment, Vote> votes;
	
	public CommentAndVotes(Answer answer, List<Comment> comments, List<Object[]> votes) {
		
		CommentComparator comparator = new CommentComparator();
		this.votes = new TreeMap<>(comparator);
		
		for (Object[] objects : votes) {
			Comment comment = (Comment) objects[0];
			Vote vote = (Vote) objects[1];
			this.votes.put(comment, vote);
		}
		
		for (Comment comment : comments) {
			if(!this.votes.containsKey(comment))
				this.votes.put(comment, null);
		}
	
	}
	
	public Map<Comment, Vote> getVotes() {
		return this.votes;
	}

}

package org.mamute.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotesToTarget {
	Map<User, VotesFromAuthor> votesToTarget = new HashMap<User, VotesFromAuthor>();
	private final VoteType votesType;
	
	public VotesToTarget(VoteType votesType) {
		this.votesType = votesType;
	}

	public VotesFromAuthor to(User target) {
		VotesFromAuthor votesFromAuthor = votesToTarget.get(target);
		if (votesFromAuthor == null) 
			return new VotesFromAuthor();
		return votesFromAuthor;
	}

	public void put(User target, List<Vote> votes) {
		User author = null;
		for (Vote vote : votes) {
			if(!votesType.equals(vote.getType())) throw new IllegalArgumentException("This object can access votes with type = "+ votesType +" only");
			author = vote.getAuthor();
		}
		
		VotesFromAuthor fromAuthor = new VotesFromAuthor();
		fromAuthor.put(author, votes);
		votesToTarget.put(target, fromAuthor);
	}
}

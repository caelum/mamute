package org.mamute.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotesFromAuthor {
	Map<User, List<Vote>> votesFromAuthor = new HashMap<User, List<Vote>>();

	public List<Vote> from(User author) {
		List<Vote> votes = votesFromAuthor.get(author) == null ? new ArrayList<Vote>() : votesFromAuthor.get(author);
		Collections.sort(votes, new VoteComparator());
		return votes;
	}

	public void put(User author, List<Vote> votes) {
		votesFromAuthor.put(author, votes);
	}

}

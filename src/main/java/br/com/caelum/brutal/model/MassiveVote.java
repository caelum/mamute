package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import br.com.caelum.vraptor.ioc.ApplicationScoped;

@ApplicationScoped
public class MassiveVote {
	
	private static final int MAX_VOTE_ALLOWED = 5;
	private static final int MIN_DAY = 2;
	
	Map<User, List<Vote>> upvotesByTarget = new HashMap<>();
	Map<User, List<Vote>> downvotesByTarget = new HashMap<>();

	public boolean shouldCountKarma(User author, User target, Vote current) {

		Map<User, List<Vote>> votesByTarget;
		if(current.getType().equals(VoteType.UP)) votesByTarget = upvotesByTarget;
		else votesByTarget = downvotesByTarget;
		
		List<Vote> votes = votesByTarget.get(target);
		if(votes == null) votes = new ArrayList<Vote>();
		votes = getVotesFrom(votes, author);
		
		Collections.sort(votes, new VoteComparator());
			
		DateTime antonte = new DateTime().minusDays(MIN_DAY);
			
		if(votes.size() == MAX_VOTE_ALLOWED) {
			if(votes.get(0).getCreatedAt().isAfter(antonte)) {
				return false;
			}
			else {
				votes.remove(0);
				votes.add(current);
				return true;
			}
		} 
		else if (votes.size() < MAX_VOTE_ALLOWED){
			votes.add(current);
			votesByTarget.put(target, votes);
			return true;
		}
		
		throw new IllegalArgumentException("massive vote size are bigger than MAX_VOTE_ALLOWED");
	}
	
	private List<Vote> getVotesFrom(List<Vote> votes, User author) {
		List<Vote> votesFromAuthor = new ArrayList<>();
		
		for (Vote vote : votes) {
			if (vote.getAuthor().equals(author)) {
				votesFromAuthor.add(vote);
			}
		}
		
		return votesFromAuthor;
	}

	public int getMaxVotesAllowed() {
		return MAX_VOTE_ALLOWED;
	}
}

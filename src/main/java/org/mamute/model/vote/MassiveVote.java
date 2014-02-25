package org.mamute.model.vote;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.joda.time.DateTime;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;
import org.mamute.model.VotesToTarget;

@ApplicationScoped
public class MassiveVote {
	
	public static final int MAX_VOTE_ALLOWED = 4;
	public static final int MIN_DAY = 1;
	
	private VotesToTarget upvotesToTarget = new VotesToTarget(VoteType.UP);
	private VotesToTarget downvotesToTarget = new VotesToTarget(VoteType.DOWN);

	public boolean shouldCountKarma(User author, User target, Vote current) {
		List<Vote> votes = getVotesWith(current.getType()).to(target).from(author);
		
		if (votes.size() == MAX_VOTE_ALLOWED) {
			Vote oldestVote = votes.get(0);
			if (isExpired(oldestVote)) {
				votes.remove(0);
				votes.add(current);
				return true;
			} else {
				return false;
			}
		} 
		else if (votes.size() < MAX_VOTE_ALLOWED) {
			votes.add(current);
			getVotesWith(current.getType()).put(target, votes);
			return true;
		}
		
		throw new IllegalArgumentException("massive vote size is bigger than "+MAX_VOTE_ALLOWED);
	}

	private VotesToTarget getVotesWith(VoteType voteType) {
		return VoteType.UP.equals(voteType) ? upvotesToTarget : downvotesToTarget;
	}

	private boolean isExpired(Vote oldestVote) {
		DateTime expirationDate = new DateTime().minusDays(MIN_DAY);
		return oldestVote.getCreatedAt().isBefore(expirationDate);
	}

	public int getMaxVotesAllowed() {
		return MAX_VOTE_ALLOWED;
	}
}

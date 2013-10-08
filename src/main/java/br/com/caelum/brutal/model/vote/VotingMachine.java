package br.com.caelum.brutal.model.vote;

import javax.inject.Inject;

import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.ReputationEventContext;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutal.reputation.rules.ReceivedVoteEvent;
import br.com.caelum.brutal.reputation.rules.VotedAtSomethingEvent;

public class VotingMachine {
    private VoteDAO votes;
    private KarmaCalculator karmaCalculator;
	private ReputationEventDAO reputationEvents;
	private MassiveVote voteChecker;

	@Deprecated
	public VotingMachine() {
	}

	@Inject
    public VotingMachine(VoteDAO votes, KarmaCalculator karmaCalculator, ReputationEventDAO reputationEvents, MassiveVote voteChecker) {
        this.votes = votes;
        this.karmaCalculator = karmaCalculator;
		this.reputationEvents = reputationEvents;
		this.voteChecker = voteChecker;
    }

    public void register(Votable votable, Vote current, Class<?> votableType) {
        User voter = current.getAuthor();
        User votableAuthor = votable.getAuthor();
        ReputationEventContext eventContext = votes.contextOf(votable);
        if (votable.getAuthor().getId().equals(voter.getId())) {
            throw new IllegalArgumentException("an author can't vote its own votable");
        }
        
        Vote previous = votes.previousVoteFor(votable.getId(), voter, votableType);

        boolean shouldCountKarma = voteChecker.shouldCountKarma(voter, votableAuthor, current);
        
        if (previous != null) {
            ReputationEvent receivedVote = new ReceivedVoteEvent(previous.getType(), votable, eventContext, shouldCountKarma).reputationEvent();
			votableAuthor.descreaseKarma(karmaCalculator.karmaFor(receivedVote));
			ReputationEvent votedAtSomething = new VotedAtSomethingEvent(previous, eventContext).reputationEvent();
            voter.descreaseKarma(karmaCalculator.karmaFor(votedAtSomething));
            reputationEvents.delete(receivedVote);
            reputationEvents.delete(votedAtSomething);
        }
        votable.substitute(previous, current);
        
        ReputationEvent receivedVote = new ReceivedVoteEvent(current.getType(), votable, eventContext, shouldCountKarma).reputationEvent();
        votableAuthor.increaseKarma(karmaCalculator.karmaFor(receivedVote));
        ReputationEvent votedAtSomething = new VotedAtSomethingEvent(current, eventContext).reputationEvent();
        voter.increaseKarma(karmaCalculator.karmaFor(votedAtSomething));
        reputationEvents.save(receivedVote);
        reputationEvents.save(votedAtSomething);
    }

}

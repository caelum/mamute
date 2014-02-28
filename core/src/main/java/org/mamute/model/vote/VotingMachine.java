package org.mamute.model.vote;

import javax.inject.Inject;

import org.mamute.controllers.RetrieveKarmaDownvote;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dao.VoteDAO;
import org.mamute.model.ReputationEvent;
import org.mamute.model.ReputationEventContext;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.interfaces.Votable;
import org.mamute.reputation.rules.KarmaCalculator;
import org.mamute.reputation.rules.ReceivedVoteEvent;
import org.mamute.reputation.rules.VotedAtSomethingEvent;

public class VotingMachine {
    private VoteDAO votes;
    private KarmaCalculator karmaCalculator;
	private ReputationEventDAO reputationEvents;
	private MassiveVote voteChecker;
	private RetrieveKarmaDownvote retrieveDownvote;

	@Deprecated
	public VotingMachine() {
	}

	@Inject
    public VotingMachine(VoteDAO votes, KarmaCalculator karmaCalculator, ReputationEventDAO reputationEvents, MassiveVote voteChecker, RetrieveKarmaDownvote retrieveDownvote) {
        this.votes = votes;
        this.karmaCalculator = karmaCalculator;
		this.reputationEvents = reputationEvents;
		this.voteChecker = voteChecker;
		this.retrieveDownvote = retrieveDownvote;
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

        if (votable.getVoteCount() <= -5) {
        	votable.getQuestion().remove();
        	retrieveDownvote.retrieveKarma(votable.getVotes());
        }
    }
    
    public void unRegister(Votable votable, Vote current, Class<?> votableType) {
        User voter = current.getAuthor();
        User votableAuthor = votable.getAuthor();
        ReputationEventContext eventContext = votes.contextOf(votable);
        if (votable.getAuthor().getId().equals(voter.getId())) {
            throw new IllegalArgumentException("an author can't unvote its own votable since it can't even vote on it");
        }
        
        Vote previous = votes.previousVoteFor(votable.getId(), voter, votableType);

        boolean shouldCountKarma = voteChecker.shouldCountKarma(voter, votableAuthor, current);
        
        /* O previous vai sempre existir nessa caso !! ( o ideal :]  ) */
        if (previous != null) {
            ReputationEvent receivedVote = new ReceivedVoteEvent(previous.getType(), votable, eventContext, shouldCountKarma).reputationEvent();
			votableAuthor.descreaseKarma(karmaCalculator.karmaFor(receivedVote));
			ReputationEvent votedAtSomething = new VotedAtSomethingEvent(previous, eventContext).reputationEvent();
            voter.descreaseKarma(karmaCalculator.karmaFor(votedAtSomething));
            reputationEvents.delete(receivedVote);
            reputationEvents.delete(votedAtSomething);
            votable.remove(previous);
        }
        
//        ReputationEvent receivedVote = new ReceivedVoteEvent(current.getType(), votable, eventContext, shouldCountKarma).reputationEvent();
//        votableAuthor.increaseKarma(karmaCalculator.karmaFor(receivedVote));
//        ReputationEvent votedAtSomething = new VotedAtSomethingEvent(current, eventContext).reputationEvent();
//        voter.increaseKarma(karmaCalculator.karmaFor(votedAtSomething));
//        reputationEvents.save(receivedVote);
//        reputationEvents.save(votedAtSomething);

//        if (votable.getVoteCount() <= -5) {
//        	votable.getQuestion().remove();
//        	retrieveDownvote.retrieveKarma(votable.getVotes()); 
//        }
    }

}

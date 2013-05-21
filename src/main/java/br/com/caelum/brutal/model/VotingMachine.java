package br.com.caelum.brutal.model;

import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutal.reputation.rules.ReceivedVoteEvent;
import br.com.caelum.brutal.reputation.rules.VotedAtSomethingEvent;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class VotingMachine {
    private VoteDAO votes;
    private final KarmaCalculator karmaCalculator;
	private final ReputationEventDAO reputationEvents;

    public VotingMachine(VoteDAO votes, KarmaCalculator karmaCalculator, ReputationEventDAO reputationEvents) {
        this.votes = votes;
        this.karmaCalculator = karmaCalculator;
		this.reputationEvents = reputationEvents;
    }

    public void register(Votable votable, Vote current, Class<?> votableType) {
        User voter = current.getAuthor();
        User votableAuthor = votable.getAuthor();
        Question questionInvolved = votes.questionOf(votable);
        if (votable.getAuthor().getId().equals(voter.getId())) {
            throw new IllegalArgumentException("an author can't vote its own votable");
        }
        
        Vote previous = votes.previousVoteFor(votable.getId(), voter, votableType);

        if (previous != null) {
            ReputationEvent receivedVote = new ReceivedVoteEvent(previous.getType(), votable, questionInvolved).reputationEvent();
			votableAuthor.descreaseKarma(karmaCalculator.karmaFor(receivedVote));
			ReputationEvent votedAtSomething = new VotedAtSomethingEvent(previous, votable, questionInvolved).reputationEvent();
            voter.descreaseKarma(karmaCalculator.karmaFor(votedAtSomething));
            reputationEvents.delete(receivedVote);
            reputationEvents.delete(votedAtSomething);
        }
        votable.substitute(previous, current);
        
        ReputationEvent receivedVote = new ReceivedVoteEvent(current.getType(), votable, questionInvolved).reputationEvent();
        votableAuthor.increaseKarma(karmaCalculator.karmaFor(receivedVote));
        ReputationEvent votedAtSomething = new VotedAtSomethingEvent(current, votable, questionInvolved).reputationEvent();
        voter.increaseKarma(karmaCalculator.karmaFor(votedAtSomething));
        reputationEvents.save(receivedVote);
        reputationEvents.save(votedAtSomething);
    }

}

package br.com.caelum.brutal.model;

import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class VotingMachine {
    private VoteDAO votes;

    public VotingMachine(VoteDAO votes) {
        this.votes = votes;
    }

    public void register(Votable votable, Vote current, Class<?> votableType) {
        User voter = current.getAuthor();
        if (votable.getAuthor().getId().equals(voter.getId())) {
            throw new IllegalArgumentException("an author can't vote its own votable");
        }
        
        Vote previous = votes.previousVoteFor(votable.getId(), voter, votableType);

        votable.substitute(previous, current);
        votes.substitute(previous, current, votable);
    }

}

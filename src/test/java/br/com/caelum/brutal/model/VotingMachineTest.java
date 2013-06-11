package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.TestCase;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;

public class VotingMachineTest extends TestCase {

    private VoteDAO votes;
    private VotingMachine votingMachine;
    private User voter;
    private User author;
    private Votable votable;
    private QuestionBuilder question = new QuestionBuilder();
    
    @Before
    public void setUp() {
        votes = mock(VoteDAO.class);
        ReputationEventDAO reputationEvents = mock(ReputationEventDAO.class);
		votingMachine = new VotingMachine(votes, new KarmaCalculator(), reputationEvents, new MassiveVote());
        voter = user("chico", "chico@brutal.com", 1l);
        author = user("author", "author@brutal.com", 2l);
        votable = question.withTitle("title").withDescription("description").withAuthor(author).build();
    }

    @Test
    public void should_add_vote() {
        votingMachine.register(votable, new Vote(voter, VoteType.UP), Question.class);
        
        assertEquals(1, votable.getVoteCount()); 
    }
    
    @Test
    public void should_substitute_vote() {
        Vote previousVote = vote(voter, VoteType.UP, 1l);
        votingMachine.register(votable, previousVote, Question.class);
        when(votes.previousVoteFor(votable.getId(), voter, Question.class)).thenReturn(previousVote);
        
        Vote newVote = new Vote(voter, VoteType.UP);
        votingMachine.register(votable, newVote, Question.class);
        
        assertEquals(1, votable.getVoteCount()); 
    }
    
    @Test
    public void should_substitute_vote_and_decrease_count() {
        Vote previousVote = vote(voter, VoteType.UP, 1l);
        votingMachine.register(votable, previousVote, Question.class);
        when(votes.previousVoteFor(votable.getId(), voter, Question.class)).thenReturn(previousVote);
        
        Vote newVote = new Vote(voter, VoteType.DOWN);
        votingMachine.register(votable, newVote, Question.class);
        
        assertEquals(-1, votable.getVoteCount()); 
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void should_disallow_author_to_vote() throws Exception {
        Vote newVote = new Vote(author, VoteType.DOWN);
        votingMachine.register(votable, newVote, Question.class);
        fail("should throw illegal argument exception");
    }
    
    @Test
	public void should_decrese_karma_of_downvoter() throws Exception {
    	Vote newVote = new Vote(voter, VoteType.DOWN);
    	votingMachine.register(votable, newVote, Question.class);
    	assertEquals(-2l, voter.getKarma());
	}
    
    @Test
    public void should_recalculate_karma_of_downvoter() throws Exception {
    	Vote previousVote = new Vote(voter, VoteType.DOWN);
		votingMachine.register(votable, previousVote, Question.class);
    	when(votes.previousVoteFor(votable.getId(), voter, Question.class)).thenReturn(previousVote);
    	assertEquals(-2l, voter.getKarma());
    	votingMachine.register(votable, new Vote(voter, VoteType.UP), Question.class);
    	assertEquals(0l, voter.getKarma());
    }

}

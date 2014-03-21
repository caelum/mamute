package org.mamute.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;
import org.mamute.model.vote.MassiveVote;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

public class MassiveVoteTest extends TestCase {
	
	private MassiveVote massiveVote;
	private User author;
	private User massiveVoter;

	@Before
	public void setUp() throws Exception {
		massiveVote = new MassiveVote();
		author = user("Felipe", "gato-sem-gata@lalala.bla");
		massiveVoter = user("MassiveWolter", "massive-wolter@bla.bla");
	}

	@Test
	public void should_not_increase_karma_for_massive_votes() {
		for (int i = 0; i < massiveVote.getMaxVotesAllowed(); i++) {
			Vote currentVote = vote(massiveVoter, VoteType.DOWN, 1l);
			assertTrue(massiveVote.shouldCountKarma(massiveVoter, author, currentVote));
		}
		Vote deniedVote = vote(massiveVoter, VoteType.DOWN, 1l);
		assertFalse(massiveVote.shouldCountKarma(massiveVoter, author, deniedVote));
	}
	
	@Test
	public void should_count_karma_if_vote_was_created_before_min_date() throws Exception {
		DateTime antantonte = new DateTime().minusDays(3);
		
		TimeMachine.goTo(antantonte).andExecute(new Block<Vote>() {
			@Override
			public Vote run() {
				for (int i = 0; i < massiveVote.getMaxVotesAllowed(); i++) {
					massiveVote.shouldCountKarma(massiveVoter, author, vote(massiveVoter, VoteType.DOWN, 1l));
				}
				return null;
			}
		});
		
		Vote acceptedVote = vote(massiveVoter, VoteType.DOWN, 1l);
		assertTrue(massiveVote.shouldCountKarma(massiveVoter, author, acceptedVote));

	}
}

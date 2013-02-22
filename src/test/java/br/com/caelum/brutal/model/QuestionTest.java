package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;

public class QuestionTest  extends TestCase{
	private QuestionBuilder question = new QuestionBuilder();

	@Test(expected = RuntimeException.class)
	public void can_not_be_marked_as_solved_by_the_an_answer_that_is_not_mine() {
		Question shouldILiveForever = question.build();
		Answer yes = answer("", null, null);
		shouldILiveForever.markAsSolvedBy(yes);
	}

	@Test
	public void can_be_marked_as_solved_by_the_an_answer_that_is_mine() {
		Question shouldILiveForever = question.build();
		Answer yes = answer("my answer", shouldILiveForever, null);
		
		shouldILiveForever.markAsSolvedBy(yes);
		
		assertEquals(yes, shouldILiveForever.getSolution());
	}
	
	@Test
	public void should_be_touched_when_marked_as_solved() {
		Question shouldILiveForever = question.build();
		User leo = new User("", "", "");
		Answer yes = answer("my answer", shouldILiveForever, leo);
		
		assertEquals(User.GHOST, shouldILiveForever.getLastTouchedBy());

		shouldILiveForever.markAsSolvedBy(yes);
		
		assertEquals(leo, shouldILiveForever.getLastTouchedBy());
	}

	@Test
	public void should_remove_vote_values_and_update_vote_count() {
		Question myQuestion = question.build();
		assertEquals(0l, myQuestion.getVoteCount());
		Vote firstVote = new Vote(null, VoteType.UP);
		myQuestion.substitute(null, firstVote);
		assertEquals(1l, myQuestion.getVoteCount());
		myQuestion.substitute(firstVote, new Vote(null, VoteType.DOWN));
		assertEquals(-1l, myQuestion.getVoteCount());
		myQuestion.substitute(null, new Vote(null, VoteType.DOWN));
		assertEquals(-2l, myQuestion.getVoteCount());
	}
	
	@Test
	public void should_be_touched_by_original_author_after_edit() throws Exception {
		User artur = new User("artur", "artur@x.com", "");
		artur.setId(1l);
		Question comoFaz = question.withTitle("titulo").withDescription("descricao").withAuthor(artur).build();
		User leo = new User("leo", "leo@x.com", "");
		leo.setId(2l);
		comoFaz.updateWith(new QuestionInformationBuilder().with(leo).build());
		assertEquals(comoFaz.getLastTouchedBy().getId(), artur.getId());
	}
	
	@Test
	public void should_update_information_status_and_last_touched_by() throws Exception {
		User artur = new User("", "", "");
		User leo = new User("", "", "");
		artur.setId(1l);
		leo.setId(2l);
		
		Question comoFaz = question.withTitle("titulo").withDescription("descricao").withAuthor(artur).build();
		QuestionInformation comoFazEditedInformation = new QuestionInformationBuilder().with(leo).build();
		comoFaz.updateWith(comoFazEditedInformation);
		comoFaz.approve(comoFazEditedInformation);
		
		assertEquals(comoFaz.getLastTouchedBy().getId(), leo.getId());
	}
	
}

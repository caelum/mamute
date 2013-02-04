package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AnswerAndVotesTest {
	
	@Test
	public void should_fill_in_only_empty_spaces_with_null() {
		Question q = new Question();
		
		Object[] first = new Object[]{answerFor(q,1 ), mock(Vote.class)};
		Object[] second = new Object[]{answerFor(q,2), mock(Vote.class)};
		Answer third = answerFor(q,3);

		List<Object[]> voteList = new ArrayList<>();
		voteList.add(first);
		voteList.add(second);

		List<Answer> answers = new ArrayList<>();
		answers.add((Answer) first[0]);
		answers.add((Answer) second[0]);
		answers.add(third);
		
		AnswerAndVotes votes = new AnswerAndVotes(q, answers, voteList);
		Map<Answer, Vote> map = votes.getVotes();
		assertEquals(first[1], map.get(first[0]));
		assertEquals(second[1], map.get(second[0]));
		assertEquals(null, map.get(third));
		assertTrue(map.containsKey(third));
	}

	private Answer answerFor(Question q, long id) {
		return new Answer("", q, null).setId(id);
	}

}

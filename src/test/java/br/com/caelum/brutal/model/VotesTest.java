package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class VotesTest {
	
	@Test
	public void should_fill_in_only_empty_spaces_with_null() {
		Object[] first = new Object[]{mock(Answer.class), mock(Vote.class)};
		Object[] second = new Object[]{mock(Answer.class), mock(Vote.class)};
		Answer third = mock(Answer.class);

		List<Object[]> voteList = new ArrayList<>();
		voteList.add(first);
		voteList.add(second);

		List<Answer> answers = new ArrayList<>();
		answers.add((Answer) first[0]);
		answers.add((Answer) second[0]);
		answers.add(third);
		
		Votes votes = new Votes(answers, voteList);
		Map<Answer, Vote> map = votes.getVotes();
		assertEquals(first[1], map.get(first[0]));
		assertEquals(second[1], map.get(second[0]));
		assertEquals(null, map.get(third));
		assertTrue(map.containsKey(third));
	}

}

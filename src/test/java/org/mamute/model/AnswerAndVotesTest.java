package org.mamute.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.AnswerAndVotes;
import org.mamute.model.Question;
import org.mamute.model.Vote;

public class AnswerAndVotesTest  extends TestCase{
	private QuestionBuilder question = new QuestionBuilder();
	
	@Test
	public void should_fill_value_with_null_only_answers_that_has_no_currentUserVote() {
		Question q = question.build();
		
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
		return answer("", q, null).setId(id);
	}

}

package org.mamute.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AnswerAndVotes {
	private final Map<Answer, Vote> votes;

	public AnswerAndVotes(Question question, List<Answer> answers, List<Object[]> votes) {
		AnswerComparator comparator = new AnswerComparator(question);
		this.votes = new TreeMap<>(comparator);
		for (Object[] objects : votes) {
			Answer answer = (Answer) objects[0];
			Vote vote = (Vote) objects[1];
			this.votes.put(answer, vote);
		}
		for (Answer answer : answers) {
			if (!this.votes.containsKey(answer)) {
				this.votes.put(answer, null);
			}
		}
	}

	public Map<Answer, Vote> getVotes() {
		return votes;
	}

}

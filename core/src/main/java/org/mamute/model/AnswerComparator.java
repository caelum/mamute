package org.mamute.model;

import java.util.Comparator;

public class AnswerComparator implements Comparator<Answer> {

	private final Question question;

	AnswerComparator(Question question) {
		this.question = question;
	}

	@Override
	public int compare(Answer o1, Answer o2) {
		if (o1.equals(o2) || o1.getId().equals(o2.getId()))
			return 0;
		if (o1.equals(question.getSolution()))
			return -1;
		if (o2.equals(question.getSolution()))
			return 1;
		if (o1.getVoteCount() != o2.getVoteCount())
			return o1.getVoteCount() > o2.getVoteCount() ? -1 : 1;
		return o1.getId() < o2.getId() ? -1 : 1;
	}

}

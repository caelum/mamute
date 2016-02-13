package org.mamute.event;

import org.mamute.model.Question;

public class QuestionCreated {
	private final Question question;

	public QuestionCreated(Question question) {
		this.question = question;
	}

	public Question getQuestion() {
		return question;
	}
}

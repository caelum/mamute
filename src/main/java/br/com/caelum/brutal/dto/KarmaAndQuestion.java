package br.com.caelum.brutal.dto;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Question;

public class KarmaAndQuestion {
	private final Question question;
	private final Long karma;
	private DateTime date;

	public KarmaAndQuestion(Question question, Long karma, DateTime date) {
		this.question = question;
		this.karma = karma;
		this.date = date;
	}
	
	public Long getKarma() {
		return karma;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public DateTime getDate() {
		return date;
	}
}
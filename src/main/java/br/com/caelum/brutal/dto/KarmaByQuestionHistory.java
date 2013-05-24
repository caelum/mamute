package br.com.caelum.brutal.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Question;

public class KarmaByQuestionHistory {

	private List<KarmaAndQuestion> history = new ArrayList<>();

	public KarmaByQuestionHistory(List<Object[]> results) {
		for (Object[] entry : results) {
			Question q = (Question) entry[0];
			Long karma = (Long) entry[1];
			DateTime date = (DateTime) entry[2];
			history.add(new KarmaAndQuestion(q, karma, date));
		}
	}
	
	public List<KarmaAndQuestion> getHistory() {
		return history;
	}

	public static class KarmaAndQuestion {
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

}

package br.com.caelum.brutal.dto;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.ReputationEventContext;

public class KarmaAndContext {
	private final ReputationEventContext context;
	private final Long karma;
	private DateTime date;

	public KarmaAndContext(ReputationEventContext context, Long karma, DateTime date) {
		this.context = context;
		this.karma = karma;
		this.date = date;
	}
	
	public Long getKarma() {
		return karma;
	}
	
	public ReputationEventContext getContext() {
		return context;
	}
	
	public DateTime getDate() {
		return date;
	}
}
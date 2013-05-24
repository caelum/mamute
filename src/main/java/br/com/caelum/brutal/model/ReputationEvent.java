package br.com.caelum.brutal.model;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class ReputationEvent {

	public static final ReputationEvent IGNORED_EVENT = new ReputationEvent(EventType.IGNORED, null, null);

	@Id @GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EventType type;
	
	private int karmaReward;

	@ManyToOne
	private Question questionInvolved;
	
	@ManyToOne
	private User user;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date = new DateTime();

	@Deprecated
	ReputationEvent() {
	}

	public ReputationEvent(EventType type, Question questionInvolved, User user) {
		this.type = type;
		this.karmaReward = type.reward();
		this.questionInvolved = questionInvolved;
		this.user = user;
	}
	
	public Integer getKarmaReward() {
		return karmaReward;
	}
	
	public Long getId() {
		return id;
	}
	
	public EventType getType() {
		return type;
	}
	
	public Question getQuestionInvolved() {
		return questionInvolved;
	}
	
	public User getUser() {
		return user;
	}
	
}

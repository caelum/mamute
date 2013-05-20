package br.com.caelum.brutal.model;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ReputationEvent {
	
	public static final ReputationEvent VOID_EVENT = new ReputationEvent(EventType.VOID, null, null);

	@Id @GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EventType type;
	
	private int karmaReward;

	@ManyToOne
	private Question questionInvolved;
	
	@ManyToOne
	private User user;

	@Deprecated
	ReputationEvent() {
	}

	public ReputationEvent(EventType type, Question questionInvolved, User user) {
		this.type = type;
		this.karmaReward = type.reward();
		this.questionInvolved = questionInvolved;
		this.user = user;
	}
	
	public int getKarmaReward() {
		return karmaReward;
	}
	
}

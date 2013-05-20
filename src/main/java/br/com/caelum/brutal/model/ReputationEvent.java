package br.com.caelum.brutal.model;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ReputationEvent {
	@Id @GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EventType type;
	
	private int karmaReward;

	@ManyToOne
	private Question questionInvolved;
	
	@ManyToOne
	private User user;

}

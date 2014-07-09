package org.mamute.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.providers.CustomConfigurationCreator;

@Entity
public class ReputationEvent {

	public static final ReputationEvent IGNORED_EVENT = new ReputationEvent(EventType.IGNORED, null, null);

	@Id @GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EventType type;
	
	private int karmaReward;

	@Any(metaColumn= @Column(name = "context_type"))
	@AnyMetaDef(idType = "long", metaType="string", metaValues = {
			@MetaValue(value = "QUESTION", targetEntity = Question.class),
			@MetaValue(value = "NEWS", targetEntity = News.class) 
	})
	@JoinColumn(name = "context_id")
	private ReputationEventContext context;
	
	@ManyToOne
	private User user;
	
	@Type(type = CustomConfigurationCreator.JODA_TIME_TYPE)
	private DateTime date = new DateTime();

	@Deprecated
	ReputationEvent() {
	}

	public ReputationEvent(EventType type, ReputationEventContext context, User user) {
		this.type = type;
		this.karmaReward = type.reward();
		this.context = context;
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
	
	public ReputationEventContext getContext() {
		return context;
	}
	
	public User getUser() {
		return user;
	}
	
}

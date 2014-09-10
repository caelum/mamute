package org.mamute.model;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.providers.SessionFactoryCreator;

@Cacheable
@Entity
public class AnswerInformation implements Information {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String markedDescription;

	@Lob
	@Length(min = 30)
	@NotEmpty
	private String description;

	@ManyToOne(optional = false, fetch = EAGER)
	private final User author;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@Embedded
	private Moderation moderation;

	@Enumerated(EnumType.STRING)
	private UpdateStatus status;

	private String ip;

	@NotNull(message = "answer.errors.comment.not_null")
	@Length(min = 5, message = "answer.errors.comment.length")
	@NotEmpty(message = "answer.errors.comment.length")
	@Type(type = "text")
	private String comment;

	@ManyToOne
    private Answer answer;
	
	/**
	 * @deprecated hibernate only
	 */
	AnswerInformation() {
		this(MarkedText.notMarked(""), null, "");
	}

	public AnswerInformation(MarkedText description, LoggedUser  currentUser, String comment) {
        if (currentUser == null) {
			this.author = null;
			this.ip = null;
		} else {
			this.author = currentUser.getCurrent();
			this.ip = currentUser.getIp();
		}
        this.comment = comment;
		setDescription(description);
	}
	
	public AnswerInformation(MarkedText description, LoggedUser currentUser, Answer existentAnswer, String comment) {
	    this(description, currentUser, comment);
	    setAnswer(existentAnswer);
	}

	public void moderate(User moderator, UpdateStatus status) {
		if (this.moderation != null) {
			throw new IllegalStateException("Already moderated");
		}
		this.status = status;
		this.moderation = new Moderation(moderator);
	}

	private void setDescription(MarkedText description) {
		this.description = description.getPure();
		this.markedDescription = description.getMarked();
	}

	public String getDescription() {
		return description;
	}

	public String getMarkedDescription() {
		return markedDescription;
	}

	public User getAuthor() {
		return author;
	}

	public void setInitStatus(UpdateStatus status) {
		if (this.status != null) {
			throw new IllegalStateException(
					"Status can only be setted once. Afterwards it should BE MODERATED!");
		}
		this.status = status;
	}

    void setAnswer(Answer answer) {
        this.answer = answer;
    }
    
    public DateTime getCreatedAt() {
        return createdAt;
    }
    
    public Moderatable getModeratable() {
        return getAnswer();
    }
    
	public Answer getAnswer() {
		return answer;
	}

    @Override
    public Long getId() {
        return id;
    }

    public boolean isPending() {
        return status.equals(UpdateStatus.PENDING);
    }
    
    public boolean isEdited() {
        return status.equals(UpdateStatus.EDITED);
    }
    
    public String getComment() {
        return comment;
    }

    public boolean isModerated() {
        return moderation != null;
    }

	@Override
	public String getTypeName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isBeforeCurrent() {
		return createdAt.isBefore(getAnswer().getInformation().createdAt);
	}

	public DateTime moderatedAt() {
		return moderation.getModeratedAt();
	}
	
	public UpdateStatus getStatus() {
		return status;
	}

	public void setModeratable(Moderatable moderatable) {
		this.answer = (Answer) moderatable;
	}

}

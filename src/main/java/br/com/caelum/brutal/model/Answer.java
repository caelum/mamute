package br.com.caelum.brutal.model;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Answer implements Votable, Commentable, Updatable, Subscribable, Touchable {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User author;
	
	@ManyToOne
	private Question question;

	@ManyToOne(optional = false)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private AnswerInformation information = null;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime lastUpdatedAt = new DateTime();

	@ManyToOne
	private User lastTouchedBy = null;

	@OneToMany(mappedBy="answer")
	@Cascade(SAVE_UPDATE)
	private List<AnswerInformation> history= new ArrayList<>();
	
	@JoinTable(name="Answer_Votes")
    @OneToMany
    private final List<Vote> votes = new ArrayList<>();

	@JoinTable(name="Answer_Comments")
	@OneToMany(cascade=CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();
    
	private long voteCount= 0;

	public Answer(AnswerInformation information, Question question, User author) {
		this.question = question;
		this.author = author;
		touchedBy(author);
		enqueueChange(information, UpdateStatus.NO_NEED_TO_APPROVE);
		information.setAnswer(this);
	}

	/**
     * @deprecated hibernate eyes only
     */
    public Answer() {
    }

    public Question getQuestion() {
		return question;
	}


	public User getAuthor() {
		return author;
	}
	
	
	public String getDescription() {
		return information.getDescription();
	}

	public String getMarkedDescription() {
		return information.getMarkedDescription();
	}

	public Long getId() {
		return id;
	}
	
	public void markAsSolution(){
		this.question.markAsSolvedBy(this);
	}
	
	public boolean isSolution() {
		return (this.question.isSolved() && this.question.getSolution().equals(this));
	}

    @Override
    public void substitute(Vote previous,Vote vote) {
    	this.voteCount += vote.substitute(previous, votes);
    }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public long getVoteCount() {
		return voteCount;
	}

	@Override
	public Comment add(Comment comment) {
		this.comments.add(comment);
		return comment;
	}
	
	@Override
	public String getTypeName() {
		return "Answer";
	}
	
	@Override
	public List<Comment> getComments() {
		return comments;
	}

	public Answer setId(Long id) {
		this.id = id;
		return this;
	}

    public Class<?> getType() {
        return Answer.class;
    }

    @Override
    public String toString() {
        return "Answer [id=" + id + "]";
    }

	public UpdateStatus updateWith(AnswerInformation information) {
		return new Updater().update(this, information);
	}

	void enqueueChange(AnswerInformation newInformation, UpdateStatus status) {
		if (status.equals(UpdateStatus.NO_NEED_TO_APPROVE)) {
			this.information = newInformation;
		}
		newInformation.setAnswer(this);
        newInformation.setInitStatus(status);
		this.history.add(newInformation);
		this.touchedBy(newInformation.getAuthor());
	}

	public void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

    @Override
    public String getTrimmedContent() {
        String markedDescription = getMarkedDescription();
        if (markedDescription.length() < 90)
            return markedDescription;
        return markedDescription.substring(0, 90) + "...";
    }

    @Override
    public DateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getTypeNameKey() {
        return "answer.type_name";
    }

    @Override
    public DateTime getLastUpdatedAt() {
    	return lastUpdatedAt;
    }
    
    @Override
    public User getLastTouchedBy() {
		return lastTouchedBy;
	}

    public UpdateStatus approve(UpdatableInformation approved, User moderator) {
        if (!approved.getClass().getSimpleName().startsWith("AnswerInformation")) {
            throw new IllegalArgumentException("an answer can only approve an answer information");
        }
        AnswerInformation approvedAnswer = (AnswerInformation) approved;
        UpdateStatus status = moderator.canUpdate(this);
        if (status == UpdateStatus.REFUSED)
            return status;
        this.touchedBy(approvedAnswer.getAuthor());
        approved.moderate(moderator, UpdateStatus.APPROVED);
        setInformation(approvedAnswer);
        return UpdateStatus.APPROVED;
    }

    private void setInformation(AnswerInformation approved) {
        this.information = approved;
    }

}

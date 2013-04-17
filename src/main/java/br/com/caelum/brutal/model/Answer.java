package br.com.caelum.brutal.model;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
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

import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.model.interfaces.Subscribable;
import br.com.caelum.brutal.model.interfaces.Votable;

@Entity
public class Answer extends Moderatable implements Post, Subscribable {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User author;
	
	@ManyToOne
	private Question question;

	@ManyToOne(optional = false, fetch = EAGER)
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

    @Embedded
	private final AnswerCommentList comments = new AnswerCommentList();
    
	private long voteCount= 0;

	@JoinTable(name = "Answer_Flags")
	@OneToMany
	private final List<Flag> flags = new ArrayList<>();
	
	@Embedded
	private final ModerationOptions moderationOptions = new ModerationOptions();
	
	public Answer(AnswerInformation information, Question question, User author) {
		this.question = question;
		this.author = author;
		touchedBy(author);
		question.add(this);
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
	
	public void markAsSolution() {
		this.question.markAsSolvedBy(this);
	}

	public void removeSolution() {
		if(!this.isSolution()) throw new IllegalStateException("The answer "+ this +"  must be a solution to call removeSolution() method");
		this.question.removeSolution();
	}
	
	public boolean isSolution() {
		return (this.question.isSolved() && this.question.getSolution().equals(this));
	}
	
	public boolean isInvisible(){
		return moderationOptions.isVisible();
	}

    @Override
    public void substitute(Vote previous, Vote vote) {
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
	public List<Comment> getVisibleCommentsFor(User user) {
		return comments.getVisibleCommentsFor(user);
	}
	
	public Answer setId(Long id) {
		this.id = id;
		return this;
	}

    public Class<? extends Votable> getType() {
        return Answer.class;
    }

    @Override
    public String toString() {
        return "Answer [id=" + id + "]";
    }

	public UpdateStatus updateWith(AnswerInformation information) {
		return new Updater().update(this, information);
	}

	public void enqueueChange(AnswerInformation newInformation, UpdateStatus status) {
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

    @Override
    public AnswerInformation getInformation() {
        return information;
    }

	@Override
	protected void updateApproved(Information approved) {
		AnswerInformation approvedAnswer = (AnswerInformation)approved;
		this.touchedBy(approvedAnswer.getAuthor());
		this.information = approvedAnswer;
	}

	@Override
	public boolean isEdited() {
		return history.size() > 1;
	}
	
	public String getTypeName() {
	    return Answer.class.getSimpleName();
	}

    public boolean isTheSameAuthorOfQuestion() {
        return getQuestion().wasMadeBy(author);
    }

	@Override
	public void add(Flag flag) {
		flags.add(flag);
	}

	@Override
	public boolean alreadyFlaggedBy(User user) {
		for (Flag flag : flags) {
			if (flag.createdBy(user))
				return true;
		}
		return false;
	}
	
	@Override
	public void remove() {
		this.moderationOptions.remove();
	}
	
	@Override
	public boolean isVisible() {
		return this.moderationOptions.isVisible();
	}

}

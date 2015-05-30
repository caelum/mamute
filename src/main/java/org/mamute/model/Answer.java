package org.mamute.model;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.*;
import org.joda.time.DateTime;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.model.interfaces.Notifiable;
import org.mamute.model.interfaces.Votable;
import org.mamute.providers.SessionFactoryCreator;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="cache")
@SQLDelete(sql = "update Answer set deleted = true, question_id=NULL where id = ?")
@Where(clause = "deleted = 0")
@Entity
public class Answer extends Moderatable implements Post, Notifiable {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User author;
	
	@ManyToOne
	private Question question;

	@ManyToOne(optional = false, fetch = EAGER)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private AnswerInformation information = null;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
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

	@OneToMany
	private Set<Attachment> attachments = new HashSet<>();

	private boolean deleted;

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

    public Question getMainThread() {
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

	public void uncheckAsSolution() {
		if (!this.isSolution()) { 
			throw new IllegalStateException("The answer "+ this +"  must be a solution to call removeSolution() method");
		}
		this.question.removeSolution();
	}
	
	public boolean isSolution() {
		return (this.question.isSolved() && this.question.getSolution().equals(this));
	}
	
    @Override
    public void substitute(Vote previous, Vote vote) {
    	this.voteCount += vote.substitute(previous, votes);
    	this.question.addUserInteraction(vote.getAuthor());
    }
    
    public void remove(Vote previous) {
		votes.remove(previous);
		this.voteCount -= previous.getCountValue();
//		addUserInteraction(vote.getAuthor());
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
		this.getQuestion().addUserInteraction(comment.getAuthor());
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

	public UpdateStatus updateWith(AnswerInformation information, Updater updater) {
		return updater.update(this, information);
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
	public void deleteComment(Comment comment) {
		this.comments.delete(comment);
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
		return lastUpdatedAt.isAfter(createdAt);
	}
	
	public String getTypeName() {
	    return Answer.class.getSimpleName();
	}

    public boolean isTheSameAuthorOfQuestion() {
        return getMainThread().wasMadeBy(author);
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
		question.subtractAnswer();
	}
	
	@Override
	public boolean isVisible() {
		return this.moderationOptions.isVisible();
	}

	@Override
	public boolean isVisibleForModeratorAndNotAuthor(User user) {
		return !this.isVisible() && !user.isAuthorOf(this);
	}
	
	public boolean hasPendingEdits() {
		for (AnswerInformation information : history) {
			if(information.isPending()) return true;
		}
		return false;
	}

	@Override
	public String getEmailTemplate() {
		return "answer_notification_mail";
	}

	@Override
	protected void addHistory(Information information) {
		this.history.add((AnswerInformation) information);
	}

	@Override
	public Question getQuestion() {
		return question;
	}

	@Override
	public List<Vote> getVotes() {
		return votes;
	}
	
	public List<Flag> getFlags() {
		return flags;
	}

	public void add(List<Attachment> attachments) {
		this.attachments.addAll(attachments);
	}

	public void removeAttachments() {
		this.attachments.clear();
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void remove(Attachment attachment) {
		this.attachments.remove(attachment);
	}

	public void replace(List<Attachment> attachmentsLoaded) {
		this.removeAttachments();
		this.add(attachmentsLoaded);
	}

	public boolean hasAuthor(User user) {
		return user.getId().equals(author.getId());
	}

	public boolean isDeletable() {
		return voteCount == 0 && !this.isSolution();
	}

	public List<Comment> getAllComments() {
		return this.comments.getAll();
	}
}

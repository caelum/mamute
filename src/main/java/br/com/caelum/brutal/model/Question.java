package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.sanitizer.QuotesSanitizer.sanitize;
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

import br.com.caelum.brutal.model.interfaces.Commentable;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.brutal.model.interfaces.Taggable;
import br.com.caelum.brutal.model.interfaces.Touchable;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.brutal.sanitizer.QuotesSanitizer;

@Entity
public class Question extends Moderatable implements Votable, Commentable, Touchable, Taggable, Flaggable {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private QuestionInformation information = null;
	
	@OneToMany(mappedBy="question")
	@Cascade(SAVE_UPDATE)
	private List<QuestionInformation> history = new ArrayList<>();
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime lastUpdatedAt = new DateTime();

	@ManyToOne
	private User lastTouchedBy = null;

	@ManyToOne(optional = true)
	private Answer solution;

	@ManyToOne
	private User author;

	@OneToMany(mappedBy = "question")
	private final List<Answer> answers = new ArrayList<>();

	private long views = 0;
	
	@JoinTable(name = "Question_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();

	private long voteCount = 0l;

	@JoinTable(name = "Question_Comments")
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Comment> comments = new ArrayList<>();

	@JoinTable(name = "Question_Flags")
	@OneToMany
	private final List<Flag> flags = new ArrayList<>();
	
	
	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this.information = null;
	}

	public Question(QuestionInformation questionInformation, User author) {
		this.author = author;
		enqueueChange(questionInformation, UpdateStatus.NO_NEED_TO_APPROVE);
	}

	@Override
	public String toString() {
		return "Question [title=" + information.getTitle() + ", createdAt=" + createdAt + "]";
	}

	public void setAuthor(User author) {
		if (this.author != null)
			return;
		this.author = author;
		touchedBy(author);
	}

	public void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

	void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void add(Answer a) {
	    answers.add(a);
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public DateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public User getLastTouchedBy() {
		return lastTouchedBy;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	void ping() {
		this.views++;
	}

	protected void markAsSolvedBy(Answer answer) {
		if (!answer.getQuestion().equals(this))
			throw new RuntimeException("Can not be solved by this answer");
		this.solution = answer;
		touchedBy(answer.getAuthor());
	}

	public Answer getSolution() {
		return solution;
	}

	public int getAnswersCount() {
		return getAnswers().size();
	}

	public boolean isSolved() {
		return solution != null;
	}

	@Override
	public void substitute(Vote previous, Vote vote) {
		this.voteCount += vote.substitute(previous, votes);
	}

	@Override
	public long getVoteCount() {
		return voteCount;
	}

	public User getAuthor() {
		return author;
	}

	@Override
	public Comment add(Comment comment) {
		this.comments.add(comment);
		return comment;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public String getTitle() {
		return information.getTitle();
	}

	public String getDescription() {
		return information.getDescription();
	}

	public String getSluggedTitle() {
		return information.getSluggedTitle();
	}

	public String getMarkedDescription() {
		return information.getMarkedDescription();
	}

	public String getSanitizedDescription() {
		return sanitize(information.getDescription());
	}

	public String getTagsAsString() {
		return information.getTagsAsString();
	}
	
	@Override
	public QuestionInformation getInformation() {
		return information;
	}
	
	public List<Tag> getTags() {
		return information.getTags();
	}
	
	public String getMostImportantTag(){
		return information.getTags().get(0).getName();
	}

	public UpdateStatus updateWith(QuestionInformation information) {
	    return new Updater().update(this, information);
	}

	public void enqueueChange(QuestionInformation newInformation, UpdateStatus status) {
		if(status.equals(UpdateStatus.NO_NEED_TO_APPROVE)) {
			this.touchedBy(newInformation.getAuthor());
			setInformation(newInformation);
		}
		newInformation.setQuestion(this);
        newInformation.setInitStatus(status);
		this.history.add(newInformation);
	}
	
	public List<QuestionInformation> getHistory() {
		return history;
	}
	
	private void setInformation(QuestionInformation information) {
		if(this.information != null){
			for (Tag tag : this.information.getTags()) {
				tag.decrementUsage();
			}
		}
		for (Tag tag : information.getTags()) {
			tag.incrementUsage();
		}
        this.information = information;
    }
	
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	protected void updateApproved(Information approved) {
		QuestionInformation approvedQuestion = (QuestionInformation) approved;
		this.touchedBy(approvedQuestion.getAuthor());
		this.information = approvedQuestion;		
	}

	@Override
	public boolean isEdited() {
		return history.size() > 1;
	}
	
	public String getTypeName() {
        return getType().getSimpleName();
    }

    @Override
    public Class<? extends Votable> getType() {
        return Question.class;
    }

    public boolean wasMadeBy(User author) {
        return this.author.getId().equals(author.getId());
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

}

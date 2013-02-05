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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.sun.istack.internal.NotNull;

@Entity
public class Question implements Votable, Commentable, Updatable {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private QuestionInformation information = null;
	
	@OneToMany
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

	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this.information = null;
	}

	public Question(String title, String description, User author) {
		this(new QuestionInformation(title, description, author, new ArrayList<Tag>()), author);
		this.author = author;
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

	public void setId(Long id) {
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

	public void ping() {
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

	public boolean hasSolution() {
		return solution != null;
	}

	@Override
	public void substitute(Vote previous, Vote vote) {
		this.voteCount = vote.substitute(previous, votes, voteCount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

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

	public String getTypeName() {
		return "Question";
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

	public String getTagsAsString() {
		return information.getTagsAsString();
	}
	
	public QuestionInformation getInformation() {
		return information;
	}

	public UpdateStatus updateWith(QuestionInformation information) {
        UpdateStatus status = information.getAuthor().canUpdate(this);
        if (status == UpdateStatus.REFUSED)
            return status;
        
        this.enqueueChange(information, status);
        return status;
	}

	public void enqueueChange(QuestionInformation newInformation, UpdateStatus status) {
		if(status.equals(UpdateStatus.NO_NEED_TO_APPROVE)) {
			this.information = newInformation;
		}
        newInformation.setInitStatus(status);
		this.history.add(newInformation);
		this.touchedBy(newInformation.getAuthor());
	}

	public List<QuestionInformation> getHistory() {
		return history;
	}
    
}

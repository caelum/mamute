package br.com.caelum.brutal.model;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class Answer implements Votable, Commentable, Updatable, Notifiable {
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

	@OneToMany
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
	}

	public Answer(String text, Question question, User author) {
		this(new AnswerInformation(text, author), question, author);
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
		return (this.question.hasSolution() && this.question.getSolution().equals(this));
	}

    @Override
    public void substitute(Vote previous,Vote vote) {
    	this.voteCount = vote.substitute(previous, votes, voteCount);
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
	
	public long getVoteCount() {
		return voteCount;
	}

	@Override
	public Comment add(Comment comment) {
		this.comments.add(comment);
		return comment;
	}

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

    @Override
    public Set<User> subscribed() {
        List<Answer> answers = this.question.getAnswers();
        Set<User> users = new HashSet<>();
        for (Answer answer : answers) {
            User author = answer.getAuthor();
            if (!this.author.equals(author)) {
                users.add(author);
            }
        }
        users.add(question.getAuthor());
        return users;
    }

	public UpdateStatus updateWith(AnswerInformation information) {
		return new Updater().update(this, information);
	}

	void enqueueChange(AnswerInformation newInformation, UpdateStatus status) {
		if(status.equals(UpdateStatus.NO_NEED_TO_APPROVE)) {
			this.information = newInformation;
		}
        newInformation.setInitStatus(status);
		this.history.add(newInformation);
		this.touchedBy(newInformation.getAuthor());
	}

	public void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

}

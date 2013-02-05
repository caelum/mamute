package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Answer implements Votable, Commentable, Updatable, Notifiable {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Lob
	@NotEmpty
	@Length(min=15)
	private String text;
	
	@ManyToOne
	private User author;
	
	@ManyToOne
	private Question question;

	@Lob
    private String htmlText;
	
	@JoinTable(name="Answer_Votes")
    @OneToMany
    private final List<Vote> votes = new ArrayList<>();

	@JoinTable(name="Answer_Comments")
	@OneToMany(cascade=CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();
    
	private long voteCount= 0;

	public Answer(String text, Question question, User author) {
        setText(text);
        this.author = author;
        this.question = question;
    }

	private void setText(String text) {
		this.text = text;
        this.htmlText = MarkDown.parse(text);
	}

	/**
     * @deprecated hibernate eyes only
     */
    public Answer() {
    }

    public Question getQuestion() {
		return question;
	}

	public String getText() {
		return text;
	}

	public User getAuthor() {
		return author;
	}
	
	public String getHtmlText() {
        return htmlText;
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

}

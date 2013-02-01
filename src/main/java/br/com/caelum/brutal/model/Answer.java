package br.com.caelum.brutal.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Answer implements Votable {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Type(type="text")
	@NotEmpty
	@Length(min=15)
	private String text;
	
	@ManyToOne
	private User author;
	
	@ManyToOne
	private Question question;

	@Type(type="text")
    private String htmlText;
	
	@JoinTable(name="Answer_Votes")
    @OneToMany
    private List<Vote> votes;

	public Answer(String text, Question question, User author) {
        this.text = text;
        this.htmlText = MarkDown.parse(text);
        this.author = author;
        this.question = question;
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
	public void addVote(Vote vote) {
	    votes.add(vote);
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


	
	
}

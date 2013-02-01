package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Answer {
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

	public Answer(String text, Question question, User author) {
        this.text = text;
        this.htmlText = MarkDown.parse(text);
        this.author = author;
        this.question = question;
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
	
	
}

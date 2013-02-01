package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

@Entity
public class Question {

	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "text")
	@Length(min = 15)
	private String title;

	@Type(type = "text")
	@Length(min = 30)
	private String description;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime lastUpdatedAt = new DateTime();

	@ManyToOne(optional = true)
	private Answer solution;

	@ManyToOne
	private User lastTouchedBy = null;

	@ManyToOne
	private User author;

	@Type(type = "text")
	private String sluggedTitle;
	
	@OneToMany(mappedBy="question")
	private List<Answer> answers;

	private long views = 0;

	@Lob
	private String markedDescription;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this("", "");
	}

	public Question(String title, String description) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
		setDescription(description);
	}

	private void setDescription(String description) {
		this.description = description;
		this.markedDescription = MarkDown.parse(description);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Question [title=" + title + ", createdAt=" + createdAt + "]";
	}

	public void setAuthor(User author) {
		if (this.author != null)
			return;
		this.author = author;
		touchedBy(author);
	}

	private void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getSluggedTitle() {
		return sluggedTitle;
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

	public String getMarkedDescription() {
		return markedDescription;
	}

	public void markAsSolvedBy(Answer answer) {
		if (!answer.getQuestion().equals(this))
			throw new RuntimeException("Can not be solved by this answer");
		this.solution = answer;
		touchedBy(answer.getAuthor());
	}

}

package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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

	@ManyToOne
	private User lastTouchedBy = null;

	@ManyToOne
	private User author;

	@Type(type = "text")
	private String sluggedTitle;
	
	@OneToMany(mappedBy="question")
	private List<Answer> answers;

	private long views = 0;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this("", "");
	}

	public Question(String title, String description) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
		this.description = description;
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
		this.author = author;
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
	
	public void touchedBy(User user) {
		lastUpdatedAt = new DateTime();
		lastTouchedBy = user;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public void ping() {
		this.views++;
	}

}

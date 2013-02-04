package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.OverridesAttribute;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Question implements Votable, Commentable, Updatable {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "text")
	@Length(min = 15)
	@NotEmpty
	private String title;

	@Type(type = "text")
	@Length(min = 30)
	@NotEmpty
	private String description;

	@Type(type = "text")
	@NotEmpty
	private String sluggedTitle;

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

	@OneToMany(mappedBy = "question")
	private final List<Answer> answers = new ArrayList<>();

	private long views = 0;
	
	@JoinTable(name = "Question_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();

	@Lob
	private String markedDescription;

	@ManyToMany
	private final List<Tag> tags = new ArrayList<>();

	private long voteCount = 0l;

	@JoinTable(name = "Question_Comments")
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Comment> comments = new ArrayList<>();

	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this("", "");
	}

	public Question(String title, String description) {
		setTitle(title);
		setDescription(description);
	}

	private void setTitle(String title) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
	}

	private void setDescription(String description) {
		this.description = description;
		this.markedDescription = MarkDown.parse(description);
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
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
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((sluggedTitle == null) ? 0 : sluggedTitle.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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

	@Override
	public boolean update(String field, String value) {
		if (field.equals("description")) {
			setDescription(value);
			return true;
		} else if (field.equals("title")) {
			setTitle(value);
			return true;
		}
		return false;
	}

}

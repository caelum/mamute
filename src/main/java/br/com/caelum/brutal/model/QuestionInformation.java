package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class QuestionInformation implements UpdatableInformation {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	@Length(min = 15)
	@NotEmpty
	private String title;

	@Lob
	@Length(min = 30)
	@NotEmpty
	private String description;

	@Type(type = "text")
	@NotEmpty
	private String sluggedTitle;

	@ManyToOne(optional = false)
	private final User author;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Embedded
	private Moderation moderation;

	@ManyToMany
	private List<Tag> tags;
	@Lob
	private String markedDescription;

	private UpdateStatus status;

	private String ip;
	
	@ManyToOne
	private Question question;

	/**
	 * @deprecated hibernate only
	 */
	QuestionInformation() {
		this("", "", null, new ArrayList<Tag>());
	}

	public QuestionInformation(String title, String description, CurrentUser user,
			List<Tag> tags) {
        if (user == null) {
			this.author = null;
			this.ip = null;
		} else {
    		this.author = user.getCurrent();
    		this.ip = user.getIp();
		}
		setTitle(title);
		setDescription(description);
		this.tags = tags;
	}

	public QuestionInformation(String title, String description, CurrentUser author) {
		this(title, description, author, new ArrayList<Tag>());
	}

	public void moderate(User moderator, UpdateStatus status) {
		if (this.moderation != null) {
			throw new IllegalStateException("Already moderated");
		}
		this.status = status;
		this.moderation = new Moderation(moderator);
	}

	private void setTitle(String title) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
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

	public String getSluggedTitle() {
		return sluggedTitle;
	}

	public String getMarkedDescription() {
		return markedDescription;
	}

	public String getTagsAsString() {
		StringBuilder sb = new StringBuilder();
		for (Tag t : tags) {
			sb.append(t.getName());
			sb.append(" ");
		}
		return sb.toString();
	}

	public User getAuthor() {
		return author;
	}

	public void setInitStatus(UpdateStatus status) {
		if (this.status != null) {
			throw new IllegalStateException(
					"Status can only be setted once. Afterwards it should BE MODERATED!");
		}
		this.status = status;
	}

	public List<Tag> getTags() {
		return tags;
	}
	
	public DateTime getCreatedAt() {
        return createdAt;
    }
	
	public Long getId() {
        return id;
    }
	
	public Question getQuestion() {
        return question;
    }
	
	void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isPending() {
        return status == UpdateStatus.PENDING;
    }
}

package org.mamute.model;

import static javax.persistence.FetchType.EAGER;
import static org.mamute.infra.NormalizerBrutal.toSlug;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.model.interfaces.Taggable;
import org.mamute.providers.SessionFactoryCreator;
import org.mamute.validators.OptionallyEmptyTags;
	
@Cacheable
@Entity
public class QuestionInformation implements Information, Taggable {

	private static final int COMMENT_MIN_LENGTH = 5;
	public static final int DESCRIPTION_MIN_LENGTH = 30;
	public static final int TITLE_MAX_LENGTH = 150;
	public static final int TITLE_MIN_LENGTH = 15;

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	@Length(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = "question.errors.title.length")
	@NotEmpty(message = "question.errors.title.length")
	private String title;

	@Lob
	@Length(min = DESCRIPTION_MIN_LENGTH, message = "question.errors.description.length")
	@NotEmpty
	private String description;

	@Type(type = "text")
	@NotEmpty
	private String sluggedTitle;
	
	@NotNull(message = "question.errors.comment.not_null")
	@Length(min = COMMENT_MIN_LENGTH, message = "question.errors.comment.length")
	@NotEmpty(message = "question.errors.comment.length")
	@Type(type = "text")
	private String comment;

	@ManyToOne(optional = false, fetch = EAGER)
	private final User author;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@Embedded
	private Moderation moderation;

	@BatchSize(size=25)
	@OrderColumn(name = "tag_order")
	@ManyToMany
	@OptionallyEmptyTags(message = "question.errors.tags.empty")
	private List<Tag> tags = new ArrayList<>();
	
	@Lob
	private String markedDescription;

	@Enumerated(EnumType.STRING)
	private UpdateStatus status;

	private String ip;
	
	@ManyToOne
	private Question question;

	/**
	 * @deprecated hibernate only
	 */
	QuestionInformation() {
		this("", MarkedText.notMarked(""), null, new ArrayList<Tag>(), "");
	}

	public QuestionInformation(String title, MarkedText description, LoggedUser user,
			List<Tag> tags, String comment) {
        if (user == null) {
			this.author = null;
			this.ip = null;
		} else {
    		this.author = user.getCurrent();
    		this.ip = user.getIp();
		}
		setTitle(title);
		setDescription(description);
		this.comment = comment;
		this.tags = tags;
	}

	public QuestionInformation(String title, MarkedText description, LoggedUser author) {
		this(title, description, author, new ArrayList<Tag>(), "");
	}

	public void moderate(User moderator, UpdateStatus status) {
		if (status == UpdateStatus.EDITED) {
			this.status = status;
			return;
		}
		
		if (this.moderation != null) {
			throw new IllegalStateException("Already moderated");
		}
		this.status = status;
		this.moderation = new Moderation(moderator);
	}
	
	boolean isModerated() {
	    return moderation != null;
	}

	private void setTitle(String title) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
	}

	private void setDescription(MarkedText description) {
		this.description = description.getPure();
		this.markedDescription = description.getMarked();
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

	public String getTagsAsString(String separator) {
		StringBuilder sb = new StringBuilder();
		for (Tag t : tags) {
			sb.append(t.getName());
			sb.append(separator);
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
	
	public Moderatable getModeratable() {
        return getQuestion();
    }
	
	public Question getQuestion() {
		return question;
	}
	
	public UpdateStatus getStatus() {
		return status;
	}

    public boolean isPending() {
        return status == UpdateStatus.PENDING;
    }
    
    public String getComment() {
        return comment;
    }

	@Override
	public String getTypeName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isBeforeCurrent() {
		return createdAt.isBefore(question.getInformation().getCreatedAt());
	}

	public DateTime moderatedAt() {
		return moderation.getModeratedAt();
	}

	@Override
	public String toString() {
		return "QuestionInformation [id=" + id + ", author=" + author
				+ ", status=" + status + ", question=" + question + "]";
	}

	@Override
	public void setModeratable(Moderatable moderatable) {
		question = (Question) moderatable;
	}

	
	
}

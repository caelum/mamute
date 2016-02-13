package org.mamute.model;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Iterables.concat;
import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;
import static org.mamute.sanitizer.QuotesSanitizer.sanitize;

import java.util.*;

import javax.annotation.Nullable;
import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.hibernate.annotations.*;
import org.joda.time.DateTime;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.model.interfaces.RssContent;
import org.mamute.model.interfaces.Taggable;
import org.mamute.model.interfaces.ViewCountable;
import org.mamute.model.interfaces.Votable;
import org.mamute.model.interfaces.Watchable;
import org.mamute.model.watch.Watcher;
import org.mamute.providers.SessionFactoryCreator;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="cache")
@SQLDelete(sql = "update Question set deleted = true where id = ?")
@Where(clause = "deleted = 0")
@Entity
public class Question extends Moderatable implements Post, Taggable, ViewCountable, Watchable, RssContent, ReputationEventContext {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false, fetch = EAGER)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private QuestionInformation information = null;
	
	@OneToMany(mappedBy="question")
	@Cascade(SAVE_UPDATE)
	private List<QuestionInformation> history = new ArrayList<>();
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime lastUpdatedAt = new DateTime();

	@ManyToOne
	private User lastTouchedBy = null;

	@ManyToOne(optional = true, fetch = EAGER)
	private Answer solution;

	@ManyToOne(fetch = EAGER)
	private User author;

	@BatchSize(size = 10)
	@OneToMany(mappedBy = "question")
	private final List<Answer> answers = new ArrayList<>();

	private long answerCount = 0l;

	private long views = 0l;
	
	@JoinTable(name = "Question_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();

	private long voteCount = 0l;

	@Embedded
	private final QuestionCommentList comments = new QuestionCommentList();
	
	@JoinTable(name = "Question_Flags")
	@OneToMany
	private final List<Flag> flags = new ArrayList<>();
	
	@Embedded
	private final ModerationOptions moderationOptions = new ModerationOptions();

	@JoinTable(name = "Question_Watchers")
	@OneToMany
	private final List<Watcher> watchers = new ArrayList<>();
	
	@JoinTable(name = "Question_Interactions", 
			inverseJoinColumns=@JoinColumn(name="userInteractions_id"), 
			joinColumns=@JoinColumn(name="Question_id")
	)
	@ManyToMany	
	private final Set<User> userInteractions = new HashSet<>();

	@OneToMany
	private Set<Attachment> attachments = new HashSet<>();
	
    public static final long SPAM_BOUNDARY = -5;

	private boolean deleted;
    
	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this.information = null;
	}

	public Question(QuestionInformation questionInformation, User author) {
		setAuthor(author);
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
		addUserInteraction(author);
		touchedBy(author);
	}

	public void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

	void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void add(Answer a) {
		if(a.getId() != null && answers.contains(a)){
			throw new IllegalStateException("This question already have the answer with id: "+ a.getId());
		}
	    answers.add(a);
	    addUserInteraction(a.getAuthor());
	    answerCount++;
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

	@Override
	public Question getMainThread() {
		return this; //sorry, I need to get a Question from a Comment.
	}
	
	public void ping() {
		this.views++;
	}

	protected void markAsSolvedBy(Answer answer) {
		if (!answer.getMainThread().equals(this))
			throw new RuntimeException("Can not be solved by this answer");
		this.solution = answer;
	}

	protected void removeSolution() {
		this.solution = null;
	}
	
	public Answer getSolution() {
		return solution;
	}

	public long getAnswersCount() {
		return answerCount;
	}

	public boolean isSolved() {
		return solution != null;
	}

	@Override
	public void substitute(Vote previous, Vote vote) {
		this.voteCount += vote.substitute(previous, votes);
		addUserInteraction(vote.getAuthor());
	}
	
	public void remove(Vote previous) {
		votes.remove(previous);
		this.voteCount -= previous.getCountValue();
//		addUserInteraction(vote.getAuthor());
	}

	@Override
	public long getVoteCount() {
		return voteCount;
	}

	public User getAuthor() {
		return author;
	}

	@Override
	public Comment add(Comment comment) {
		this.comments.add(comment);
		addUserInteraction(comment.getAuthor());
		return comment;
	}
	
	@Override
	public List<Comment> getVisibleCommentsFor(User user) {
		return comments.getVisibleCommentsFor(user);
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

	public String getSanitizedDescription() {
		return sanitize(information.getDescription());
	}

	public String getTagsAsString(String separator) {
		return information.getTagsAsString(separator);
	}
	
	@Override
	public QuestionInformation getInformation() {
		return information;
	}
	
	public List<Tag> getTags() {
		return information.getTags();
	}
	
	public List<TagUsage> getTagsUsage() {
		ArrayList<TagUsage> tagsUsage = new ArrayList<>();
		for (Tag tag : this.getTags()) {
			tagsUsage.add(new TagUsage(tag, tag.getUsageCount()));
		}
		return tagsUsage;
	}
	
	public Tag getMostImportantTag(){
		List<Tag> tags = information.getTags();
		if(tags.isEmpty()){
			throw new IllegalStateException("a question must have at least one tag");
		}
		return tags.get(0);
	}

	public UpdateStatus updateWith(QuestionInformation information, Updater updater) {
	    return updater.update(this, information);
	}
	
	protected void addHistory(Information newInformation) {
		this.history.add((QuestionInformation) newInformation);
	}

	public List<QuestionInformation> getHistory() {
		return history;
	}
	
	private void setInformation(QuestionInformation newInformation) {
		newInformation.setModeratable(this);
		if (this.information != null) {
			for (Tag tag : this.information.getTags()) {
				tag.decrementUsage();
			}
		}
		for (Tag tag : newInformation.getTags()) {
			tag.incrementUsage();
		}
        this.information = newInformation;
    }
	
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	protected void updateApproved(Information approved) {
		QuestionInformation approvedQuestion = (QuestionInformation) approved;
		this.touchedBy(approvedQuestion.getAuthor());
		setInformation(approvedQuestion);
	}

	@Override
	public boolean isEdited() {
		return lastUpdatedAt.isAfter(createdAt);
	}
	
	public String getTypeName() {
        return getType().getSimpleName();
    }

    @Override
    public Class<? extends Votable> getType() {
        return Question.class;
    }
    
    public boolean wasMadeBy(User author) {
        return this.author.getId().equals(author.getId());
    }

	@Override
	public void add(Watcher watcher) {
		watchers.add(watcher);
	}

	@Override
	public void remove(Watcher watcher) {
		watchers.remove(watcher);
	}

	@Override
	public List<Watcher> getWatchers() {
		return watchers;
	}

	@Override
	public void add(Flag flag) {
		flags.add(flag);
	}
	
	@Override
	public boolean alreadyFlaggedBy(User user) {
		for (Flag flag : flags) {
			if (flag.createdBy(user))
				return true;
		}
		return false;
	}

	public boolean alreadyAnsweredBy(User user) {
		for (Answer answer : answers) {
			if(answer.getAuthor().getId() == user.getId()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void remove() {
		this.moderationOptions.remove();
	}
	
	@Override
	public boolean isVisible() {
		return this.moderationOptions.isVisible();
	}
	
	@Override
	public boolean isVisibleForModeratorAndNotAuthor(User user) {
		return !this.isVisible() && user != null && !user.isAuthorOf(this);
	}
	
	public boolean isVisibleFor(User user) {
		return this.isVisible() || (user != null && (user.isModerator() || user.isAuthorOf(this)));
	}

	@Override
	public boolean hasPendingEdits() {
		for (QuestionInformation information : history) {
			if(information.isPending()) return true;
		}
		return false;
	}
	
	public void subtractAnswer(){
		answerCount--;
	}

	@Override
	public String getTypeNameKey() {
		return "question.type_name";
	}

	@Override
	public void deleteComment(Comment comment) {
		this.comments.delete(comment);
	}

	public String getTrimmedContent() {
        String markedDescription = getMarkedDescription();
        if (markedDescription.length() < 200)
            return markedDescription;
        return markedDescription.substring(0, 200) + "...";
    }
    
    public boolean hasAuthor(User user) {
		return user.getId().equals(author.getId());
    }

	@Override
	public Question getQuestion() {
		return this;
	}

	public String getMetaDescription() {
		String fullMeta = getTitle() + " " + getMarkedDescription();
		int index = Math.min(fullMeta.length(), 200);
		return fullMeta.substring(0, index);
	}

	public String getLinkPath() {
		return id + "-" + getSluggedTitle();
	}

	public boolean isInactiveForOneMonth() {
		return lastUpdatedAt.isBefore(new DateTime().minusMonths(1));
	}
	
	public boolean canMarkAsSolution (User user) {
		return (user.equals(author) && solution == null && answerCount != 0); 
	}

	@Override
	public List<Vote> getVotes() {
		return votes;
	}

	public boolean hasInteraction(User user) {
		return userInteractions.contains(user);
	}

	public void addUserInteractions(List<User> users) {
		userInteractions.addAll(users);
	}

	public void addUserInteraction(User user) {
		userInteractions.add(user);
	}
	
	public List<Flag> getFlags() {
		return flags;
	}

	public boolean hasTags() {
		return !this.getTags().isEmpty();
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void add(List<Attachment> attachments) {
		this.attachments.addAll(attachments);
	}

	public void remove(Attachment attachment) {
		this.attachments.remove(attachment);
	}

	public void removeAttachments() {
		this.attachments.clear();
	}

	public boolean isDeletable() {
		return answerCount == 0 && flags.isEmpty() && comments.isEmpty();
	}

	public Iterable<Attachment> getAllAttachments() {
		Iterable<Attachment> answersAttachments = concat(transform(this.getAnswers(), getAnswerAttachments()));
		ArrayList<Attachment> allAttachments = Lists.newArrayList(answersAttachments);
		allAttachments.addAll(this.getAttachments());
		return allAttachments;
	}

	private Function<Answer, Set<Attachment>> getAnswerAttachments() {
		return new Function<Answer, Set<Attachment>>() {
			@Nullable
			@Override
			public Set<Attachment> apply(Answer input) {
				return input.getAttachments();
			}
		};
	}

	public List<Comment> getAllComments() {
		return comments.getAll();
	}
}

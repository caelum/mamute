package org.mamute.model;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.infra.NotFoundException;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.model.interfaces.RssContent;
import org.mamute.model.interfaces.ViewCountable;
import org.mamute.model.interfaces.Votable;
import org.mamute.model.interfaces.Watchable;
import org.mamute.model.watch.Watcher;
import org.mamute.providers.SessionFactoryCreator;
import org.owasp.html.HtmlPolicyBuilder;

@Entity
public class News extends Moderatable implements Post, ViewCountable, Watchable, RssContent, ReputationEventContext {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false, fetch = EAGER)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private NewsInformation information = null;
	
	@OneToMany(mappedBy="news")
	@Cascade(SAVE_UPDATE)
	private List<NewsInformation> history = new ArrayList<>();
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime lastUpdatedAt = new DateTime();

	@ManyToOne
	private User lastTouchedBy = null;

	@ManyToOne(fetch = EAGER)
	private final User author;

	private long views = 0l;
	
	@JoinTable(name = "News_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();

	private long voteCount = 0l;

	@Embedded
	private final NewsCommentList comments = new NewsCommentList();
	
	@Embedded
	private final ModerationOptions moderationOptions = new ModerationOptions();
	
	@JoinTable(name = "News_Flags")
	@OneToMany
	private final List<Flag> flags = new ArrayList<>();
	
	private boolean approved = false;
	
	@JoinTable(name = "News_Watchers")
	@OneToMany
	private final List<Watcher> watchers = new ArrayList<>();
	
	
	/**
	 * @deprecated hibernate eyes only
	 */
	public News() {
		author = null;
	}
	
	public News(NewsInformation newsInformation, User author) {
		this.author = author;
		enqueueChange(newsInformation, UpdateStatus.NO_NEED_TO_APPROVE);
	}

	@Override
	public void substitute(Vote previous, Vote current) {
		this.voteCount += current.substitute(previous, votes);
	}
	
	public void remove(Vote previous) {
		votes.remove(previous);
		this.voteCount -= previous.getCountValue();
//		addUserInteraction(vote.getAuthor());
	}

	@Override
	public User getAuthor() {
		return author;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public long getVoteCount() {
		return voteCount;
	}

	@Override
	public Class<? extends Votable> getType() {
		return News.class;
	}

	@Override
	public Comment add(Comment comment) {
		comments.add(comment);
		return comment;
	}

	@Override
	public List<Comment> getVisibleCommentsFor(User user) {
		return comments.getVisibleCommentsFor(user);
	}

	@Override
	public DateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	@Override
	public User getLastTouchedBy() {
		return lastTouchedBy;
	}

	@Override
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public boolean isEdited() {
		return lastUpdatedAt.isAfter(createdAt);
	}

	@Override
	public boolean alreadyFlaggedBy(User user) {
		for (Flag flag : flags) {
			if (flag.createdBy(user))
				return true;
		}
		return false;
	}

	@Override
	public void remove() {
		moderationOptions.remove();
	}

	@Override
	public boolean isVisible() {
		return moderationOptions.isVisible();
	}

	@Override
	public boolean isVisibleForModeratorAndNotAuthor(User user) {
		return !this.isVisible() && user != null && !user.isAuthorOf(this);
	}
	
	@Override
	public String getTypeNameKey() {
		return "news.type_name";
	}

	@Override
	public NewsInformation getInformation() {
		return information;
	}

	@Override
	protected void updateApproved(Information approved) {
		NewsInformation approvedNews = (NewsInformation) approved;
		touchedBy(approvedNews.getAuthor());
		setInformation(approvedNews);		
	}

	private void setInformation(NewsInformation approvedNews) {
		approvedNews.setNews(this);
        this.information = approvedNews;		
	}

	private void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

	@Override
	public String getTypeName() {
        return getType().getSimpleName();
    }
	
	@Override
	public boolean hasPendingEdits() {
		for (NewsInformation information : history) {
			if(information.isPending()) return true;
		}
		return false;
	}

	@Override
	public void add(Flag flag) {
		flags.add(flag);
	}

	@Override
	public Watchable getMainThread() {
		return this;
	}
	
	public String getTitle(){
		return information.getTitle();
	}

	public UpdateStatus updateWith(NewsInformation information, Updater updater) {
	    return updater.update(this, information);
	}

	public List<NewsInformation> getHistory() {
		return history;
	}

	@Override
	protected void addHistory(Information information) {
		this.history.add((NewsInformation) information);
	}

	public String getSluggedTitle() {
		return information.getSluggedTitle();
	}
	
	public String getMarkedDescription(){
		return information.getMarkedDescription();
	}
	
	public long getViews() {
		return views;
	}
	
	public News approved(){
		approved = true;
		return this;
	}
	
	public boolean isApproved(){
		return approved;
	}

	public boolean checkVisibilityFor(LoggedUser currentUser) {
		boolean isTheAuthor = currentUser.isLoggedIn() && currentUser.getCurrent().getId().equals(author.getId());
		if (isApproved() || currentUser.isModerator() || isTheAuthor) {
			return true;
		}
		throw new NotFoundException();
	}

	@Override
	public void ping() {
		views++;
	}

	@Override
	public Question getQuestion() {
		return null; //TODO dar um jeito
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
	
	public String getDescription() {
		return getInformation().getDescription();
	}

	@Override
	public String getLinkPath() {
		return "noticias/" + id + "-" + getSluggedTitle();
	}
	
    public String getTrimmedContent() {
        String markedDescription = getMarkedDescription();
        if (markedDescription.length() < 125)
            return sanitize(markedDescription);
        return sanitize(markedDescription.substring(0, 125));
    }

	private String sanitize(String markedDescription) {
		return new HtmlPolicyBuilder().toFactory().sanitize(markedDescription) + "...";
	}

	@Override
	public List<Vote> getVotes() {
		return votes;
	}

	@Override
	public void deleteComment(Comment comment) {
		this.comments.delete(comment);
	}
}


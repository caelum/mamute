package org.mamute.controllers;

import static com.google.common.base.Objects.firstNonNull;
import static java.util.Arrays.asList;
import static org.mamute.dao.WithUserPaginatedDAO.OrderType.ByDate;
import static org.mamute.dao.WithUserPaginatedDAO.OrderType.ByVotes;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.AnswerDAO;
import org.mamute.dao.PaginatableDAO;
import org.mamute.dao.QuestionDAO;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dao.TagDAO;
import org.mamute.dao.UserDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.dto.UserPersonalInfo;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;
import org.mamute.validators.UserPersonalInfoValidator;

import com.google.common.base.Objects;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class UserProfileController extends BaseController{
	
	private static final String HTTP = "http://";
	@Inject private Result result;
	@Inject private UserDAO users;
	@Inject private LoggedUser currentUser;
	@Inject private UserPersonalInfoValidator infoValidator;
	@Inject private QuestionDAO questions;
	@Inject private AnswerDAO answers;
    @Inject private TagDAO tags;
	@Inject private WatcherDAO watchers;
	@Inject private ReputationEventDAO reputationEvents;
	@Inject private MessageFactory messageFactory;

	@Get
	public void showProfile(@Load User user, String sluggedName){
		if (redirectToRightSluggedName(user, sluggedName)) {
			return;
		}
		
		result.include("isCurrentUser", currentUser.getCurrent().getId().equals(user.getId()));
		result.include("questionsByVotes", questions.postsToPaginateBy(user, ByVotes, 1));
		result.include("questionsCount", questions.countWithAuthor(user));
		result.include("answersByVotes", answers.postsToPaginateBy(user, ByVotes, 1));
		result.include("answersCount", answers.countWithAuthor(user));
		result.include("watchedQuestions", watchers.postsToPaginateBy(user, ByDate, 1));
		result.include("watchedQuestionsCount", watchers.countWithAuthor(user));
		
		result.include("reputationHistory", reputationEvents.karmaWonByQuestion(user, new DateTime().minusMonths(1), 5).getHistory());
		
		result.include("questionsPageTotal", questions.numberOfPagesTo(user));
		result.include("answersPageTotal", answers.numberOfPagesTo(user));
		result.include("watchedQuestionsPageTotal", watchers.numberOfPagesTo(user));
		
		result.include("mainTags", tags.findMainTagsOfUser(user));
		result.include("selectedUser", user);
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void reputationHistory(@Load User user, String sluggedName) {
		if (redirectToRightSluggedName(user, sluggedName)) {
			return;
		}
		result.include("selectedUser", user);
		result.include("reputationHistory", reputationEvents.karmaWonByQuestion(user).getHistory());
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
	}

	@Get
	@Path(priority=Path.LOW, value="")
	public void typeByVotesWith(Long id, String sluggedName, OrderType order, Integer p, String type){
		User author = users.findById(id);
		order = order == null ? ByVotes : order;
		Integer page = p == null ? 1 : p;
		PaginatableDAO paginatableDAO = type.equals("perguntas") ? questions : answers;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(paginatableDAO, author, order, page, type);		
	}
	
	@Get
	public void watchersByDateWith(Long id, String sluggedName, Integer p){
		User user = users.findById(id);
		Integer page = p == null ? 1 : p;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(watchers, user, ByDate, page, "acompanhadas");
	}
		
	@Get
	public void editProfile(@Load User user){
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home(null);
			return;
		}
		result.include("user", user);
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Post
	public void editProfile(@Load User user, String name, String realName, String email, 
			String website, String location, DateTime birthDate, String description, boolean isSubscribed) {
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home(null);
			return;
		}
		
		if (website != null) {
			website = correctWebsite(website);
		}

		UserPersonalInfo info = new UserPersonalInfo(user)
			.withName(name)
			.withRealName(realName)
			.withEmail(email)
			.withWebsite(website)
			.withLocation(location)
			.withBirthDate(birthDate)
			.withAbout(description)
			.withIsSubscribed(isSubscribed);
		
		if (!infoValidator.validate(info)) {
			infoValidator.onErrorRedirectTo(this).editProfile(user);
			return;
		}
		
		users.updateLoginMethod(user, email);
		
		user.setPersonalInformation(info);
		
		result.redirectTo(this).showProfile(user, user.getSluggedName());
	}
	
	@Get
	public void unsubscribe(@Load User user, String hash){
		String correctHash = user.getUnsubscribeHash();
		
		if (!correctHash.equals(hash)) {
			result.include("messages", asList(messageFactory.build("errors", "newsletter.unsubscribe_page.invalid")));
			result.redirectTo(ListController.class).home(null);
			return;
		}
		result.include("messages", asList(messageFactory.build("messages", "newsletter.unsubscribe_page.valid")));
		user.setSubscribed(false);
		result.redirectTo(ListController.class).home(null);
	}
	
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Post
	public void toogleBanned(@Load User user) {
		if(user.isBanned()){
			user.undoBan();
		}else{
			user.ban();
			users.clearSessionOf(user);
		}
		result.nothing();
	}

	private String correctWebsite(String website) {
		String protocol = "";
		if(!website.startsWith(HTTP)){
			protocol = HTTP;
		}
		website = protocol+website;
		return website;
	}
	
	private boolean redirectToRightSluggedName(User user, String sluggedName) {
		String correctSluggedName = user.getSluggedName();
		if (!correctSluggedName.equals(sluggedName)) {
			result.redirectTo(this).showProfile(user, correctSluggedName);
			return true;
		}
		return false;
	}
}

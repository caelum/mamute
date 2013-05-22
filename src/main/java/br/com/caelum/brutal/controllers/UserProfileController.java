package br.com.caelum.brutal.controllers;

import static br.com.caelum.brutal.dao.WithUserDAO.OrderType.ByDate;
import static br.com.caelum.brutal.dao.WithUserDAO.OrderType.ByVotes;

import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.dto.UserPersonalInfo;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.UserPersonalInfoValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.hibernate.extra.Load;

@Resource
public class UserProfileController {
	
	private Result result;
	private UserDAO users;
	private LoggedUser currentUser;
	private final QuestionDAO questions;
	private final AnswerDAO answers;
	private UserPersonalInfoValidator infoValidator;
    private final TagDAO tags;
	private static final String HTTP = "http://";
	private final WatcherDAO watchers;

    public UserProfileController(Result result, UserDAO users, LoggedUser currentUser,
            QuestionDAO questions, AnswerDAO answers, TagDAO tags, WatcherDAO watchers,
            UserPersonalInfoValidator infoValidator) {
		this.result = result;
		this.users = users;
		this.currentUser = currentUser;
		this.answers = answers;
		this.questions = questions;
		this.tags = tags;
		this.watchers = watchers;
		this.infoValidator = infoValidator;
	}
	
	@Get("/usuario/{user.id:[0-9]+}/{sluggedName}")
	public void showProfile(@Load User user, String sluggedName){
		String correctSluggedName = user.getSluggedName();
		if (!correctSluggedName.equals(sluggedName)){
			result.redirectTo(this).showProfile(user, correctSluggedName);
			return;
		}
		
		result.include("isCurrentUser", currentUser.getCurrent().getId().equals(user.getId()));
		result.include("questionsByVotes", questions.postsToPaginateBy(user, ByVotes, 1));
		result.include("questionsCount", questions.countWithAuthor(user));
		result.include("answersByVotes", answers.postsToPaginateBy(user, ByVotes, 1));
		result.include("answersCount", answers.countWithAuthor(user));
		result.include("watchedQuestions", watchers.postsToPaginateBy(user, ByDate, 1));
		result.include("watchedQuestionsCount", watchers.countWithAuthor(user));
		
		result.include("questionsPageTotal", questions.numberOfPagesTo(user));
		result.include("answersPageTotal", answers.numberOfPagesTo(user));
		result.include("watchedQuestionsPageTotal", watchers.numberOfPagesTo(user));
		
		result.include("mainTags", tags.findMainTagsOfUser(user));
		result.include("selectedUser", user);
	}

	@Get("/usuario/{id}/{sluggedName}/perguntas")
	public void questionsByVotesWith(Long id, String sluggedName, OrderType order, Integer p){
		User author = users.findById(id);
		order = order == null ? ByVotes : order;
		Integer page = p == null ? 1 : p;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(questions, author, order, page, "perguntas");
	}
	
	@Get("/usuario/{id}/{sluggedName}/respostas")
	public void answersByVotesWith(Long id, String sluggedName, OrderType order, Integer p){
		User author = users.findById(id);
		OrderType orderType = order == null ? ByVotes : order;
		Integer page = p == null ? 1 : p;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(answers, author, orderType, page, "respostas");
	}
	
	@Get("/usuario/{id}/{sluggedName}/acompanhadas")
	public void watchersByDateWith(Long id, String sluggedName, Integer p){
		User user = users.findById(id);
		Integer page = p == null ? 1 : p;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(watchers, user, ByDate, page, "acompanhadas");
	}
		
	@Get("/usuario/editar/{user.id:[0-9]+}")
	public void editProfile(@Load User user){
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home(null);
			return;
		}
		result.include("user", user);
	}
	
	@Post("/usuario/editar/{user.id:[0-9]+}")
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
		
		user.setPersonalInformation(info);
		
		result.redirectTo(this).showProfile(user, user.getSluggedName());
	}

	private String correctWebsite(String website) {
		String protocol = "";
		if(!website.startsWith(HTTP)){
			protocol = HTTP;
		}
		website = protocol+website;
		return website;
	}
}

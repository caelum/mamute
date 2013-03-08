package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.json;

import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.dto.UserPersonalInfo;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.UserPersonalInfoValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class UserProfileController {
	
	private Result result;
	private UserDAO users;
	private LoggedUser currentUser;
	private final QuestionDAO questions;
	private final AnswerDAO answers;
	private UserPersonalInfoValidator infoValidator;
    private final TagDAO tags;
	
    public UserProfileController(Result result, UserDAO users, LoggedUser currentUser,
            QuestionDAO questions, AnswerDAO answers, TagDAO tags,
            UserPersonalInfoValidator infoValidator) {
		this.result = result;
		this.users = users;
		this.currentUser = currentUser;
		this.answers = answers;
		this.questions = questions;
		this.tags = tags;
		this.infoValidator = infoValidator;
	}
	
	@Get("/users/{id}/{sluggedName}")
	public void showProfile(Long id, String sluggedName){
		User selectedUser = users.findById(id);
		String correctSluggedName = selectedUser.getSluggedName();
		if (!correctSluggedName.equals(sluggedName)){
			result.redirectTo(this).showProfile(id, correctSluggedName);
			return;
		}
		
		result.include("isCurrentUser", currentUser.getCurrent().getId().equals(id));
		result.include("questionsByVotes", questions.withAuthorByVotes(selectedUser));
		result.include("answersByVotes", answers.withAuthorByVotes(selectedUser));
		result.include("mainTags", tags.findMainTagsOfUser(selectedUser));
		result.include("selectedUser", selectedUser);
	}
	
	@Get("/users/{id}/{sluggedName}/questions/byVotes")
	public void questionsByVotesWith(Long id, String sluggedName){
		User author = users.findById(id);
		result.use(json()).withoutRoot().from(questions.withAuthorByVotes(author)).include("information").serialize();
	}
	
	@Get("/users/{id}/{sluggedName}/questions/byDate")
	public void questionsByDateWith(Long id, String sluggedName){
		User author = users.findById(id);
		result.use(json()).withoutRoot().from(questions.withAuthorByDate(author)).include("information").serialize();
	}
	
	@Get("/users/{id}/{sluggedName}/answers/byVotes")
	public void answersByVotesWith(Long id, String sluggedName){
		User author = users.findById(id);
		result.use(json()).withoutRoot().from(answers.withAuthorByVotes(author)).include("question").include("question.information").serialize();
	}
	
	@Get("/users/{id}/{sluggedName}/answers/byDate")
	public void answersByDateWith(Long id, String sluggedName){
		User author = users.findById(id);
		result.use(json()).withoutRoot().from(answers.withAuthorByDate(author)).include("question").include("question.information").serialize();
	}
	
	@Get("/users/edit/{id}")
	public void editProfile(Long id) {
		User user = users.findById(id);
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home();
			return;
		}
		result.include("user", user);
	}
	
	@Post("/users/edit/{id}")
	public void editProfile(Long id, String name, String realName, String email, 
			String website, String location, DateTime birthDate, String description) {
		User user = users.findById(id);
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home();
			return;
		}
		UserPersonalInfo info = new UserPersonalInfo(user, name, realName,email, website, location, birthDate, description);
		
		if (!infoValidator.validate(info)) {
			infoValidator.onErrorRedirectTo(this).editProfile(id);
			return;
		}
		
		user.setPersonalInformation(email, name, realName, website, location, birthDate, description);
		
		result.redirectTo(this).showProfile(id, user.getSluggedName());
	}
}

package br.com.caelum.brutal.controllers;

import java.util.Arrays;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class UserProfileController {
	
	private Result result;
	private UserDAO users;
	private LoggedUser currentUser;
	
	public UserProfileController(Result result, UserDAO users,
			LoggedUser currentUser) {
		super();
		this.result = result;
		this.users = users;
		this.currentUser = currentUser;
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
		result.include("selectedUser", selectedUser);
	}
	
	@Get("/users/edit/{id}")
	public void editProfile(Long id) {
		User user = users.findById(id);
		
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home();
			return;
		}
		
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy").withLocale(new Locale("pt", "br"));
		result.include("dateFormat", dateFormat);
		result.include("user", user);
	}
	
	@Post("/users/edit/{id}")
	public void editProfile(Long id, String name, String email, String website, String location, LocalDate birthDate) {
		User user = users.findById(id);
		
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home();
			return;
		}
		
		if (birthDate == null) {
			result.include("errors", Arrays.asList("user_profile.edit.validation.invalid_birth_date"));
			result.redirectTo(this).editProfile(id);
			return;
		}
		
		user.setPersonalInformation(email, name, website, location, birthDate.toDateTimeAtStartOfDay());
		result.redirectTo(this).showProfile(id, user.getSluggedName());
	}
}

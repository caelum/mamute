package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
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
}

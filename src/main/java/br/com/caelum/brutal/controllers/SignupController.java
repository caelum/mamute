package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.UserValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class SignupController {

	private final UserValidator validator;
	private final UserDAO dao;
	private final Result result;

	public SignupController(UserValidator validator, UserDAO dao, Result result) {
		this.validator = validator;
		this.dao = dao;
		this.result = result;
	}
	
	@Get("/signup")
	public void signupForm() {
	}

	@Post("/signup")
	public void signup(String email, String password, String name, String passwordConfirmation) {
		User newUser = new User(name, email, password);
		
		boolean valid = validator.validate(newUser, password, passwordConfirmation);
		validator.onErrorRedirectTo(this).signupForm();
		
		if (valid) {
		    dao.save(newUser);
		    result.include("confirmations", Arrays.asList("signup.confirmation"));
		    result.redirectTo(ListController.class).home();
		} else {
		    result.include("email", email);
		    result.include("name", name);
		}
	}
}

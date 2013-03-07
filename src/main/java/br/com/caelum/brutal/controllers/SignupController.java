package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.SignupValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class SignupController {

	private final SignupValidator validator;
	private final UserDAO users;
	private final Result result;
	private MessageFactory messageFactory;
	private LoginMethodDAO loginMethods;

	public SignupController(SignupValidator validator, UserDAO users, Result result, MessageFactory messageFactory, LoginMethodDAO loginMethods) {
		this.validator = validator;
		this.users = users;
		this.result = result;
		this.messageFactory = messageFactory;
		this.loginMethods = loginMethods;
	}
	
	@Get("/signup")
	public void signupForm() {
	}

	@Post("/signup")
	public void signup(String email, String password, String name, String passwordConfirmation) {
		User newUser = new User(name, email);
		LoginMethod brutalLogin = LoginMethod.brutalLogin(newUser, email, password);
		newUser.add(brutalLogin);
		
		boolean valid = validator.validate(newUser, password, passwordConfirmation);
		validator.onErrorRedirectTo(this).signupForm();
		
		if (valid) {
		    users.save(newUser);
		    loginMethods.save(brutalLogin);
		    result.include("messages", Arrays.asList(
		    			messageFactory.build("confirmation", "signup.confirmation")
		    		));
		    result.forwardTo(AuthController.class).login(email, password, "");
		} else {
		    result.include("email", email);
		    result.include("name", name);
		}
	}
}

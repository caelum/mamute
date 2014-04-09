package org.mamute.controllers;

import static java.util.Arrays.asList;

import javax.inject.Inject;

import org.mamute.auth.FacebookAuthService;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoginMethod;
import org.mamute.model.User;
import org.mamute.validators.SignupValidator;
import org.mamute.vraptor.Linker;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class SignupController {

	@Inject private SignupValidator validator;
	@Inject private UserDAO users;
	@Inject private Result result;
	@Inject private MessageFactory messageFactory;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private FacebookAuthService facebook;
	@Inject private Linker linker;

	@Get
	public void signupForm() {
		String facebookUrl = facebook.getOauthUrl(null);
		result.include("facebookUrl", facebookUrl);
	}

	@Post
	public void signup(String email, String password, String name, String passwordConfirmation) {
		User newUser = new User(name, email);
		LoginMethod brutalLogin = LoginMethod.brutalLogin(newUser, email, password);
		newUser.add(brutalLogin);
		
		validator.validate(newUser, password, passwordConfirmation);
		result.include("email", email);
		result.include("name", name);
		validator.onErrorRedirectTo(this).signupForm();
		
	    users.save(newUser);
	    loginMethods.save(brutalLogin);
	    result.include("messages", asList(messageFactory.build("confirmation", "signup.confirmation")));
	    linker.linkTo(ListController.class).home(null);
	    result.forwardTo(AuthController.class).login(email, password, linker.get());
	}
	
	@Get
	public void showUsageTerms(){
	}
	
	@Get
	public void privacyPolicy(){
	}
}

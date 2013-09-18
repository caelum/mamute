package br.com.caelum.brutal.controllers;

import static java.util.Arrays.asList;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.dao.LoginMethodDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.SignupValidator;
import br.com.caelum.brutal.vraptor.Linker;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;

@Controller
public class SignupController {

	@Inject private SignupValidator validator;
	@Inject private UserDAO users;
	@Inject private Result result;
	@Inject private MessageFactory messageFactory;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private FacebookAuthService facebook;
	@Inject private Linker linker;

	@Get("/cadastrar")
	public void signupForm() {
		String facebookUrl = facebook.getOauthUrl(null);
		result.include("facebookUrl", facebookUrl);
	}

	@Post("/cadastrar")
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
	
	@Get("/termos-de-uso")
	public void showUsageTerms(){
	}
	
	@Get("/privacidade")
	public void privacyPolicy(){
	}
}

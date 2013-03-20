package br.com.caelum.brutal.controllers;

import static java.util.Arrays.asList;

import java.util.List;

import br.com.caelum.brutal.auth.Access;
import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.auth.SignupInfo;
import br.com.caelum.brutal.dao.LoginMethodDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.MethodType;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;

@Resource
public class FacebookAuthController {
	
	private final FacebookAuthService facebook;
	private final UserDAO users;
	private final LoginMethodDAO loginMethods;
	private final Result result;
	private final Access access;
	private final MessageFactory messageFactory;

	public FacebookAuthController(FacebookAuthService facebook, UserDAO users, 
			LoginMethodDAO loginMethods, Result result, Access access,
			MessageFactory messageFactory) {
		this.facebook = facebook;
		this.users = users;
		this.loginMethods = loginMethods;
		this.result = result;
		this.access = access;
		this.messageFactory = messageFactory;
	}
	
	@Get("/signup/facebook")
	public void signupViaFacebook(String code) {
		String rawToken = facebook.buildToken(code);
		SignupInfo signupInfo = facebook.getSignupInfo();

		User existantFacebookUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.FACEBOOK);
		if (existantFacebookUser != null) {
			access.login(existantFacebookUser);
			result.redirectTo(ListController.class).home();
			return;
		}
		
		User existantBrutalUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.BRUTAL);
		if (existantBrutalUser != null) {
			mergeLoginMethods(rawToken, existantBrutalUser);
			return;
		}
		
		createNewUser(rawToken, signupInfo);
		
		result.redirectTo(ListController.class).home();
	}

	private void createNewUser(String rawToken, SignupInfo signupInfo) {
		User user = new User(signupInfo.getName(), signupInfo.getEmail());
		LoginMethod facebookLogin = new LoginMethod(MethodType.FACEBOOK, signupInfo.getEmail(), rawToken, user);
		user.add(facebookLogin);
		
		users.save(user);
		loginMethods.save(facebookLogin);
		access.login(user);
	}

	private void mergeLoginMethods(String rawToken, User existantBrutalUser) {
		LoginMethod facebookLogin = LoginMethod.facebookLogin(existantBrutalUser, existantBrutalUser.getEmail(), rawToken);
		List<I18nMessage> messages = asList(messageFactory.build("confirmation", "signup.facebook.existant_brutal", existantBrutalUser.getEmail()));
		result.include("messages", messages);
		existantBrutalUser.add(facebookLogin);
		loginMethods.save(facebookLogin);
		access.login(existantBrutalUser);
		result.redirectTo(ListController.class).home();
	}
}

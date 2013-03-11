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
			LoginMethodDAO loginMethods, Result result, Access access, MessageFactory messageFactory) {
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

		User existantUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.FACEBOOK, MethodType.BRUTAL);
		if (existantUser != null) {
			access.login(existantUser);
			List<I18nMessage> messages = asList(messageFactory.build("confirmation", "signup.facebook.already_signed"));
			result.include("messages", messages);
			result.redirectTo(ListController.class).home();
			return;
		}
		
		User user = new User(signupInfo.getName(), signupInfo.getEmail());
		LoginMethod facebookLogin = new LoginMethod(MethodType.FACEBOOK, signupInfo.getEmail(), rawToken, user);
		user.add(facebookLogin);
		
		users.save(user);
		loginMethods.save(facebookLogin);
		access.login(user);
		result.redirectTo(ListController.class).home();
		
	}
}

package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.auth.SignupInfo;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.MethodType;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

public class FacebookSignupController {
	
	private final FacebookAuthService facebook;
	private final UserDAO users;
	private final LoginMethodDAO loginMethods;
	private final Result result;

	public FacebookSignupController(FacebookAuthService facebook, UserDAO users, LoginMethodDAO loginMethods, Result result) {
		this.facebook = facebook;
		this.users = users;
		this.loginMethods = loginMethods;
		this.result = result;
	}
	
	@Get("/signup/facebook")
	public void signupViaFacebook(String code) {
		String rawToken = facebook.buildToken(code);
		SignupInfo signupInfo = facebook.getSignupInfo();
		
		User user = new User(signupInfo.getName(), signupInfo.getEmail());
		LoginMethod facebookLogin = new LoginMethod(MethodType.FACEBOOK, signupInfo.getEmail(), rawToken, user);
		user.add(facebookLogin);
		
		users.save(user);
		loginMethods.save(facebookLogin);
		
		result.redirectTo(ListController.class).home();
	}
}

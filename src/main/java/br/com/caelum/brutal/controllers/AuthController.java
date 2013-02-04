package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import br.com.caelum.brutal.auth.DefaultAuthenticator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AuthController {
	
	
	private final DefaultAuthenticator auth;
	private final Result result;
	public AuthController(DefaultAuthenticator auth, Result result) {
		this.auth = auth;
		this.result = result;
	}
	
	@Post("/login")
	public void login(String email, String password, String redirectUrl) {
		if(auth.authenticate(email, password)) {
			redirectToRightUrl(redirectUrl);
		}else{
			result.include("alerts", Arrays.asList("auth.invalid.login"));
			result.redirectTo(ListController.class).home();
		}
	}
	
	@Get("/logout")
	public void logout() {
		auth.signout();
		result.redirectTo(ListController.class).home();
	}
	
	private void redirectToRightUrl(String redirectUrl) {
		if(redirectUrl != null && !redirectUrl.isEmpty()){
			result.redirectTo(redirectUrl);
		}else{
			result.redirectTo(ListController.class).home();
		}
	}
}

package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import br.com.caelum.brutal.auth.DefaultAuthenticator;
import br.com.caelum.brutal.model.User;
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
	
	@Get("/")
	public void root() {
	}
	
	@Post("/login")
	public void login(String email, String password) {
		if(auth.authenticate(email, password)) {
			result.redirectTo(ListController.class).home();
			return;
		}
		result.include("alerts", Arrays.asList("auth.invalid.login"));
		result.forwardTo(this).root();
	}
	
	
	@Get("/logout")
	public void logout() {
		auth.signout();
		result.redirectTo(this).root();
	}

}

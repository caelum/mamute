package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.DefaultAuthenticator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
public class AuthController extends Controller {
	
	private final DefaultAuthenticator auth;
	public AuthController(DefaultAuthenticator auth) {
		this.auth = auth;
	}
	
	@Get("/login")
	public void loginForm(){
	}
	
	@Post("/login")
	public void login(String email, String password, String redirectUrl) {
		if (auth.authenticate(email, password)) {
			redirectToRightUrl(redirectUrl);
		} else {
			includeAsList("messages", i18n("alert", "auth.invalid.login"));
			include("redirectUrl", redirectUrl);
			redirectTo(this).loginForm();
		}
	}
	
	@Get("/logout")
	public void logout() {
		auth.signout();
		redirectTo(ListController.class).home();
	}
	
	private void redirectToRightUrl(String redirectUrl) {
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            redirectTo(redirectUrl);
        } else {
            redirectTo(ListController.class).home();
        }
	}
}

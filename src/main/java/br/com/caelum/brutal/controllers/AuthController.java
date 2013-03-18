package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.DefaultAuthenticator;
import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AuthController extends Controller {
	
	private final DefaultAuthenticator auth;
	private final FacebookAuthService facebook;
	private final Result result;
	public AuthController(DefaultAuthenticator auth, FacebookAuthService facebook, Result result) {
		this.auth = auth;
		this.facebook = facebook;
		this.result = result;
	}
	
	@Get("/login")
	public void loginForm(String redirectUrl) {
		String facebookUrl = facebook.getOauthUrl();
		//TODO: verify that redirectUrl is inside our domain to avoid phishing atacks
		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			include("redirectUrl", redirectUrl);
		}
		result.include("facebookUrl", facebookUrl);
	}
	
	@Post("/login")
	public void login(String email, String password, String redirectUrl) {
		if (auth.authenticate(email, password)) {
			redirectToRightUrl(redirectUrl);
		} else {
			includeAsList("messages", i18n("error", "auth.invalid.login"));
			redirectTo(this).loginForm(redirectUrl);
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

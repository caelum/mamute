package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.DefaultAuthenticator;
import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.validators.UrlValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;

@Resource
public class AuthController extends Controller {
	
	private final DefaultAuthenticator auth;
	private final FacebookAuthService facebook;
	private final Result result;
	private final UrlValidator urlValidator;
	private final Validator validator;
	public AuthController(DefaultAuthenticator auth, FacebookAuthService facebook, 
			Result result, UrlValidator urlValidator, Validator validator) {
		this.auth = auth;
		this.facebook = facebook;
		this.result = result;
		this.urlValidator = urlValidator;
		this.validator = validator;
	}
	
	@Get("/login")
	public void loginForm(String redirectUrl) {
		String facebookUrl = facebook.getOauthUrl();
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
		urlValidator.validate(redirectUrl);
		validator.onErrorRedirectTo(ListController.class).home();
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            redirectTo(redirectUrl);
        } else {
            redirectTo(ListController.class).home();
        }
	}
}

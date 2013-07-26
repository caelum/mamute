package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.DefaultAuthenticator;
import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.brutauth.auth.rules.LoggedRule;
import br.com.caelum.brutal.validators.UrlValidator;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AuthController extends Controller {
	
	private final DefaultAuthenticator auth;
	private final FacebookAuthService facebook;
	private final Result result;
	private final UrlValidator urlValidator;
	
	public AuthController(DefaultAuthenticator auth, FacebookAuthService facebook, 
			Result result, UrlValidator urlValidator) {
		this.auth = auth;
		this.facebook = facebook;
		this.result = result;
		this.urlValidator = urlValidator;
	}
	
	@Get("/login")
	public void loginForm(String redirectUrl) {
		String facebookUrl = facebook.getOauthUrl(redirectUrl);
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
	
	@CustomBrutauthRules(LoggedRule.class)
	@Get("/logout")
	public void logout() {
		auth.signout();
		redirectTo(ListController.class).home(null);
	}
	
	private void redirectToRightUrl(String redirectUrl) {
		boolean valid = urlValidator.isValid(redirectUrl);
		if (!valid) {
			includeAsList("messages", i18n("error", "error.invalid.url", redirectUrl));
		}
        if (redirectUrl != null && !redirectUrl.isEmpty() && valid) {
            redirectTo(redirectUrl);
        } else {
            redirectTo(ListController.class).home(null);
        }
	}
}

package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.auth.DefaultAuthenticator;
import org.mamute.auth.FacebookAuthService;
import org.mamute.brutauth.auth.rules.LoggedRule;
import org.mamute.validators.LoginValidator;
import org.mamute.validators.UrlValidator;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class AuthController extends BaseController {
	
	@Inject private DefaultAuthenticator auth;
	@Inject private FacebookAuthService facebook;
	@Inject private Result result;
	@Inject private UrlValidator urlValidator;
	@Inject private LoginValidator validator;
	
	@Get
	public void loginForm(String redirectUrl) {
		String facebookUrl = facebook.getOauthUrl(redirectUrl);
		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			include("redirectUrl", redirectUrl);
		}
		result.include("facebookUrl", facebookUrl);
	}
	
	@Post
	public void login(String email, String password, String redirectUrl) {
		if (validator.validate(email, password) && auth.authenticate(email, password)) {
			redirectToRightUrl(redirectUrl);
		} else {
			includeAsList("messages", i18n("error", "auth.invalid.login"));
			redirectTo(this).loginForm(redirectUrl);
			validator.onErrorRedirectTo(this).loginForm(redirectUrl);
		}
	}
	
	@CustomBrutauthRules(LoggedRule.class)
	@Get
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

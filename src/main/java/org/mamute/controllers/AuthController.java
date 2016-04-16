package org.mamute.controllers;

import java.util.Collections;

import javax.inject.Inject;

import org.mamute.auth.AuthenticationException;
import org.mamute.auth.Authenticator;
import org.mamute.auth.FacebookAuthService;
import org.mamute.auth.GoogleAuthService;
import org.mamute.brutauth.auth.rules.LoggedRule;
import org.mamute.model.LoggedUser;
import org.mamute.validators.LoginValidator;
import org.mamute.validators.UrlValidator;

import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class AuthController extends BaseController {
	@Inject	private Authenticator auth;
	@Inject	private FacebookAuthService facebook;
	@Inject	private GoogleAuthService google;
	@Inject	private Result result;
	@Inject	private UrlValidator urlValidator;
	@Inject	private LoginValidator validator;
	@Inject private LoggedUser loggedUser;

	@Public
	@Get
	public void loginForm(String redirectUrl) {
		if (loggedUser.isLoggedIn()) {
			result.include("loginRequiredMessages", Collections.emptyList());
			redirectToRightUrl(redirectUrl);
		} else {
			String facebookUrl = facebook.getOauthUrl(redirectUrl);
			String googleUrl = google.getOauthUrl(redirectUrl);
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				include("redirectUrl", redirectUrl);
			}
			result.include("facebookUrl", facebookUrl);
			result.include("googleUrl", googleUrl);
		}
	}

	@Public
	@Post
	public void login(String email, String password, String redirectUrl) {
		try {
			if (validator.validate(email, password) && auth.authenticate(email, password)) {
				redirectToRightUrl(redirectUrl);
			} else {
				includeAsList("mamuteMessages", i18n("error", "auth.invalid.login"));
				validator.onErrorRedirectTo(this).loginForm(redirectUrl);
				redirectTo(this).loginForm(redirectUrl);
			}
		} catch (AuthenticationException e) {
			includeAsList("mamuteMessages", i18n("error", "auth.configuration.error", e.getAuthType()));
			validator.onErrorRedirectTo(this).loginForm(redirectUrl);
			redirectTo(this).loginForm(redirectUrl);
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
			includeAsList("mamuteMessages", i18n("error", "error.invalid.url", redirectUrl));
		}
		if (redirectUrl != null && !redirectUrl.isEmpty() && valid) {
			redirectTo(redirectUrl);
		} else {
			redirectTo(ListController.class).home(null);
		}
	}
}

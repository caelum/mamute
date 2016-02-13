package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.auth.FacebookAPI;
import org.mamute.auth.SocialAPI;
import org.mamute.model.MethodType;
import org.mamute.qualifiers.Facebook;
import org.mamute.validators.UrlValidator;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Public
@Controller
public class FacebookAuthController extends BaseController {
	
	@Inject private UrlValidator urlValidator;
	@Inject private LoginMethodManager loginManager;
	@Inject private Result result;
	@Inject @Facebook private OAuthService service;

	@Get("/sign-up/facebook/")
	public void signupViaFacebook(String code, String state) {
		if (code == null) {
			includeAsList("mamuteMessages", i18n("error", "error.signup.facebook.unknown"));
			redirectTo(SignupController.class).signupForm();
			return;
		}
		
		Token token = service.getAccessToken(null, new Verifier(code));
		
		SocialAPI facebookAPI = new FacebookAPI(service, token);
		
		boolean success = loginManager.merge(MethodType.FACEBOOK, facebookAPI);
		if(!success) {
			includeAsList("mamuteMessages", i18n("error", "signup.errors.facebook.invalid_email", state));
			result.redirectTo(AuthController.class).loginForm(state);
			return;
		}
		redirectToRightUrl(state);
	}

	private void redirectToRightUrl(String state) {
		boolean valid = urlValidator.isValid(state);
		if (!valid) {
			includeAsList("mamuteMessages", i18n("error", "error.invalid.url", state));
		}
        if (state != null && !state.isEmpty() && valid) {
            redirectTo(state);
        } else {
            redirectTo(ListController.class).home(null);
        }
	}
}

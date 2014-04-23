package org.mamute.controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.mamute.auth.Access;
import org.mamute.auth.SignupInfo;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.model.LoginMethod;
import org.mamute.model.MethodType;
import org.mamute.model.User;
import org.mamute.qualifiers.Google;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Routed
@Controller
public class GoogleAuthController {
	
	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
	
	@Inject @Google private OAuthService service;
	@Inject private Result result;
	@Inject private HttpSession session;
	@Inject private UserDAO users;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Access access;
	
	@Get
	public void signUpViaGoogle() {
		Token token = service.getRequestToken();
		String url = AUTHORIZE_URL + token.getToken();
		
		session.setAttribute("requestToken", token);
		
		result.redirectTo(url);
	}
	
	@Get
	public void googleCallback(@Named("oauth_token") String token, @Named("oauth_verifier") String oAuthVerifier) {
		Token requestToken = (Token) session.getAttribute("requestToken");
		Verifier verifier = new Verifier(oAuthVerifier);
		Token accessToken = service.getAccessToken(requestToken, verifier);
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/me");
		service.signRequest(accessToken, request);
	    request.addHeader("GData-Version", "3.0");
	    Response response = request.send();
	    
	    JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
	    String email = jsonObject.get("emails").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
	    String name = jsonObject.get("displayName").getAsString();
	    String photoUrl = jsonObject.get("image").getAsJsonObject().get("url").getAsString();
	    
	    SignupInfo signupInfo = new SignupInfo(MethodType.GOOGLE, email, name, "", photoUrl);
	    
	    createNewUser(accessToken.toString(), signupInfo);
	}
	
	private void createNewUser(String rawToken, SignupInfo signupInfo) {
		User user = new User(signupInfo.getName(), signupInfo.getEmail());
		LoginMethod googleLogin = new LoginMethod(MethodType.GOOGLE, signupInfo.getEmail(), rawToken, user);
		if (signupInfo.containsPhotoUrl()) {
			user.setPhotoUri(signupInfo.getPhotoUri());
		}
		user.add(googleLogin);
		
		users.save(user);
		loginMethods.save(googleLogin);
		access.login(user);
	}
}

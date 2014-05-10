package org.mamute.integration.scene;

import com.google.gson.JsonParser;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.mamute.auth.OAuthServiceCreator;
import org.mamute.integration.pages.FacebookLoginPage;
import org.mamute.integration.pages.Home;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class FacebookSignupTest extends AcceptanceTestBase {

    private static final Logger LOG = getLogger(FacebookSignupTest.class);

	
	@Test
	public void should_signup_and_login_through_facebook() throws Exception {
        String appToken;
        try {
            appToken = getAppToken();
        }catch (NoSuchElementException exc) {
            LOG.warn("Did you remember to put your facebook APP_ID and Secret in mamute.properties?", exc.getMessage());
            throw exc;
        }
        FacebookUser facebookUser = createFacebookTestUser(appToken);
        FacebookLoginPage signupWithFacebook = home().toLoginPage().signupWithFacebook();
        Home home = signupWithFacebook
                .writeEmail(facebookUser.email)
                .writePassword(facebookUser.password)
                .submit().confirm();

        assertTrue(home.isLoggedIn());
        assertEquals(facebookUser.email, home.toProfilePage().userEmail());

        home().logOut();

        boolean loggedInThroughFacebook = home().toLoginPage().loginThroughFacebook().isLoggedIn();
        assertTrue(loggedInThroughFacebook);
    }
	
	@SuppressWarnings("deprecation")
	private FacebookUser createFacebookTestUser(String appToken) throws URIException,
			IOException, HttpException {
		String clientId = env.get(OAuthServiceCreator.FACEBOOK_CLIENT_ID);
		GetMethod method = new GetMethod("https://graph.facebook.com/"+clientId+"/accounts/test-users?" +
				"installed=true" +
				"&name=TESTUSER" +
				"&method=post" +
				"&access_token=" + URLEncoder.encode(appToken));
		int status = client.executeMethod(method);
		if (status != 200) {
			fail("could not create test user, facebook sent " + status + " status code");
		}
		return new FacebookUser(method.getResponseBodyAsString());
	}

	private String getAppToken() throws IOException, HttpException {
		String appSecret = env.get(OAuthServiceCreator.FACEBOOK_APP_SECRET);
		String clientId = env.get(OAuthServiceCreator.FACEBOOK_CLIENT_ID);
		
		GetMethod method = new GetMethod("https://graph.facebook.com/oauth/access_token" +
				"?client_id=" + clientId +
				"&client_secret=" + appSecret +
				"&grant_type=client_credentials");
		int status = client.executeMethod(method);
		if (status != 200) {
			fail("could not create app access_token");
		}
		String responseBody = method.getResponseBodyAsString();
		return responseBody.split("=")[1];
	}

	
	private static class FacebookUser {

		private String email;
		private String password;

		public FacebookUser(String json) {
			this.email = new JsonParser().parse(json).getAsJsonObject().get("email").getAsString();
			this.password = new JsonParser().parse(json).getAsJsonObject().get("password").getAsString();
		}
		
	}
}

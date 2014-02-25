package org.mamute.integration.scene.vraptor;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class AuthTest extends CustomVRaptorIntegration {

	@Test
	public void should_log_in() {
		User user = randomUser();
		UserFlow navigation = login(navigate(), user.getEmail());
		VRaptorTestResult loginResult = navigation.followRedirect().execute();
		loginResult.wasStatus(200).isValid();

		
		LoggedUser loggedUser = loginResult.getObject("currentUser");
		User currentUser = loggedUser.getCurrent();

		assertThat(currentUser.getId(), equalTo(user.getId()));
	}

	@Test
	public void should_not_log_in_with_invalid_user() {
		UserFlow navigation = login(navigate(), "invalidEmail");
		VRaptorTestResult loginResult = navigation.followRedirect().execute();
		loginResult.wasStatus(200).isValid();

		LoggedUser loggedUser = loginResult.getObject("currentUser");
		User currentUser = loggedUser.getCurrent();

		assertThat(currentUser, equalTo(User.GHOST));
	}

	@Test
	public void should_save_url_when_redirected_to_login() {
		UserFlow navigation = createQuestionPage(navigate());
		VRaptorTestResult navigationResult = navigation.followRedirect().execute();
		navigationResult.wasStatus(200).isValid();

		Elements redirectInput = getElementsByAttributeAndValue(navigationResult, "name", "redirectUrl");

		String redirectUrl = redirectInput.first().attr("value");
		String expectedUrl = rootPath(navigationResult).concat("/perguntar");

		assertThat(redirectUrl, equalTo(expectedUrl));
	}

	private Elements getElementsByAttributeAndValue(VRaptorTestResult navigationResult, String attributeName, String attributeValue) {
		Document document = Jsoup.parse(navigationResult.getResponseBody());
		return document.getElementsByAttributeValue(attributeName, attributeValue);
	}

	private String rootPath(VRaptorTestResult navigationResult) {
		String resultUrl = navigationResult.getRequest().getRequestURL().toString();
		String requestURI = navigationResult.getRequest().getRequestURI();
		return resultUrl.replace(requestURI, "");
	}

	private UserFlow createQuestionPage(UserFlow navigation) {
		return navigation.get("/perguntar");
	}

}

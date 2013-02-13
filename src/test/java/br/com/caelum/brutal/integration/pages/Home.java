package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.WebDriver;

public class Home extends PageObject {

	public Home(WebDriver driver) {
		super(driver);
	}

	public LoginPage toLoginPage(){
		allByClassName("login").get(0).click();
		LoginPage loginPage = new LoginPage(driver);
		return loginPage;
	}

	public boolean isLoggedInAs(String name) {
		return isLoggedIn() && byClassName("user-name").getText().equals(name);
	}

	public boolean isLoggedIn() {
		return allByClassName("login").isEmpty();
	}

	public SignupPage toSignUpPage() {
		byClassName("signup").click();
		SignupPage signupPage = new SignupPage(driver);
		return signupPage;
	}

	public boolean isLoadedCorrectly() {
		boolean containsQuestionList = !allByClassName("question-list").isEmpty();
		return containsQuestionList;
	}
	
	public Home logOut() {
		byClassName("logout").click();
		return this;
	}

	public NewQuestionPage toNewQuestionPage() {
		byClassName("ask-a-question").click();
		return new NewQuestionPage(driver);
	}


}

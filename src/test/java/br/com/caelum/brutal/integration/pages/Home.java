package br.com.caelum.brutal.integration.pages;

import static org.openqa.selenium.By.tagName;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

	public QuestionPage toFirstQuestionPage() {
		byClassName("item-title").findElement(tagName("a")).click();
		return new QuestionPage(driver);
	}

	public UnsolvedListPage toUnsolvedList() {
		byClassName("main-nav").findElement(By.className("unsolved")).click();
		return new UnsolvedListPage(driver);
	}

	public LoginPage toNewQuestionPageWhileNotLogged() {
		byClassName("ask-a-question").click();
		return new LoginPage(driver);
	}

	public WithTagListPage toWithTagList(String tag) {
		List<WebElement> tags = allByClassName("tag");
		for (WebElement currentTag : tags) {
			if(currentTag.getText().equals(tag)){
				currentTag.click();
				return new WithTagListPage(driver);
			}
		}
		throw new RuntimeException("Tag not found: "+tag);
	}


}

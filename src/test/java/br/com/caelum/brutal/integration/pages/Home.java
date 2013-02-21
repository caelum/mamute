package br.com.caelum.brutal.integration.pages;

import static org.openqa.selenium.By.tagName;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import br.com.caelum.pagpag.aceitacao.util.ServerInfo;

public class Home extends PageObject{
	static final ServerInfo SERVER = new ServerInfo();
	
	public Home(WebDriver driver) {
		super(driver);
		driver.get(SERVER.getRoot());
	}

	public LoginPage toLoginPage(){
		byClassName("login").click();
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

    public QuestionPage toFirstQuestionWithAnswerPage() {
        List<WebElement> questionItems = allByCSS(".question-item");
        for (WebElement question : questionItems) {
            String countText = question.findElement(By.className("answers")).getText();
            Integer answersCount = Integer.parseInt(countText.split("\\s+")[0].trim());
            if (answersCount > 0) {
                question.findElement(By.cssSelector(".title a")).click();
                return new QuestionPage(driver);
            }
        }
        throw new NoSuchElementException("could not find any question with at least one answer");
    }


}

package org.mamute.integration.pages;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.fail;
import static org.openqa.selenium.By.tagName;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.mamute.integration.util.ServerInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
		return isLoggedIn() && byClassName("user-name").getText().contains(name);
	}

	public boolean isLoggedIn() {
		WebElement profileLink = profileLink();
		String linkClass = profileLink.getAttribute("class");
		return !linkClass.equals("login");
	}

	private WebElement profileLink() {
		WebElement byCSS = byCSS(".user-item a");
		return byCSS;
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
		byClassName("question-title").findElement(tagName("a")).click();
		return new QuestionPage(driver);
	}

	public UnsolvedListPage toUnsolvedList() {
		byClassName("footer").findElement(By.className("unsolved-link")).click();
		return new UnsolvedListPage(driver);
	}

	public LoginPage toNewQuestionPageWhileNotLogged() {
		byClassName("ask-a-question").click();
		return new LoginPage(driver);
	}

	public WithTagListPage toWithTagList(String tag) {
		List<WebElement> tags = allByClassName("tag-brutal");
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
            if (hasAnswer(question)) {
                question.findElement(By.cssSelector(".title a")).click();
                return new QuestionPage(driver);
            }
        }
        throw new NoSuchElementException("could not find any question with at least one answer");
    }
	
    private boolean hasAnswer(WebElement question) {
		String countText = question.findElement(By.className("answers")).getText();
		Integer answersCount = parseInt(countText.split("\\s+")[0].trim());
		return answersCount > 0;
	}

	public UserProfilePage toProfilePage() {
		if (!isLoggedIn()) {
			fail("can't go to profile page if i'm not logged in");
		}
		profileLink().click();
		return new UserProfilePage(driver);
	}

	public List<HomeQuestion> allQuestions() {
		List<HomeQuestion> questions = new ArrayList<>();
		List<WebElement> questionItens = allByClassName("question-item");
		for (WebElement item : questionItens) {
			String title = item.findElement(By.className("title")).getText();
			String views = item.findElement(By.className("votes")).getText();
			Integer voteCount = Integer.valueOf(views.split("\n")[0].trim());
			questions.add(new HomeQuestion(title, voteCount));
		}
		return questions;
	}
	
	public static class HomeQuestion {
		private final String title;
		private final Integer voteCount;
		public HomeQuestion(String title, Integer voteCount) {
			this.title = title;
			this.voteCount = voteCount;
		}
		
		public String getTitle() {
			return title;
		}
		
		public Integer getVoteCount() {
			return voteCount;
		}
		
	}

}

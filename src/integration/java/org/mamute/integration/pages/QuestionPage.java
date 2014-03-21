package org.mamute.integration.pages;

import static java.util.Arrays.asList;
import static org.openqa.selenium.By.tagName;

import java.util.ArrayList;
import java.util.List;

import org.mamute.model.VoteType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class QuestionPage extends PageObject{

	public QuestionPage(WebDriver driver) {
		super(driver);
	}
	
	public EditQuestionPage toEditQuestionPage(){
		byClassName("edit-question").click();
		return new EditQuestionPage(driver);
	}

	public boolean hasInformation(String title, String description, String tags) {
		return hasTitle(title) && hasDescription(description) && hasTags(tags);
	}

	private boolean hasTitle(String questionTitle) {
		return questionTitle.equals(byClassName("question-title").getText());
	}
	
	private boolean hasDescription(String questionDescription) {
		return questionDescription.equals(byClassName("question-description").getText());
	}
	
	private boolean hasTags(String tags) {
		WebElement question = byClassName("question-area");
		List<String> tagNames = asList(tags.split(" "));
		List<WebElement> tagsElements = question.findElements(By.className("tag"));
		for (WebElement tagElement: tagsElements) {
			if(!tagNames.contains(tagElement.getText())) return false;
		}
		return true;
	}

    public QuestionPage voteQuestion(VoteType type) {
        int voteCount = questionVoteCount();
        String voteTypeClass = type.name().toLowerCase() + "-vote";
        byCSS(".question-area ." + voteTypeClass).click();
        Integer finalVoteCount = voteCount + type.getCountValue();
        waitForTextIn(By.cssSelector(questionVoteCountSelector()), finalVoteCount.toString(), 5);
        return this;
    }

    public int questionVoteCount() {
    	byCSS(questionVoteCountSelector()).getText();
        return Integer.parseInt(byCSS(questionVoteCountSelector()).getText());
    }

    private String questionVoteCountSelector() {
        return ".question-area .vote-count";
    }

    public QuestionPage voteFirstAnswer(VoteType type) {
        int initialVoteCount = firstAnswerVoteCount();
        String voteTypeClass = type.name().toLowerCase() + "-vote";
        byCSS(".answer ." + voteTypeClass).click();
        Integer finalVoteCount = initialVoteCount + type.getCountValue();
        waitForTextIn(By.cssSelector(".answer .vote-count"), finalVoteCount.toString(), 10);
        return this;
    }

    public int firstAnswerVoteCount() {
        String text = firstAnswer().findElement(By.className("vote-count")).getText();
        return Integer.parseInt(text);
    }


	public EditAnswerPage toEditFirstAnswerPage() {
		firstAnswer().findElement(By.className("edit")).click();
		return new EditAnswerPage(driver);
	}

	public QuestionPage answer(String description) {
		WebElement answerForm = byClassName("answer-form");
		byCSS("#wmd-input").sendKeys("");
		By buttonSelector = By.cssSelector(".same-author-confirmation button");
		if (isElementPresent(buttonSelector, answerForm)) {
			waitForClickableElement(buttonSelector, 5);
			answerForm.findElement(buttonSelector).click();
		}
		byCSS("#wmd-input").sendKeys(description);
		answerForm.submit();
		return new QuestionPage(driver);
	}

	public boolean firstAnswerHasDescription(String newDescription) {
		return newDescription.equals(firstAnswer().findElement(By.className("post-text")).getText());
	}

	private WebElement firstAnswer() {
		waitForElement(By.className("answer"), 3);
		return byClassName("answer");
	}

    public QuestionPage commentQuestion(String comment) {
        comment(".post-container", comment);
        return this;
    }

	public QuestionPage commentFirstAnswer(String comment) {
        comment(".answer .post-container", comment);
        return this;
	}

	private void comment(String postContainer, String comment) {
		byCSS(postContainer + " .add-comment").click();
        waitForElement(By.cssSelector(postContainer + " textarea[name=comment]"), 2);
        byCSS(postContainer + " textarea[name=comment]").sendKeys(comment);
        byCSS(postContainer + " .simple-ajax-form form .post-submit").click();
        waitForElement(By.cssSelector(postContainer + " .comment-container span"), 10);
	}

	public boolean isAnswerFormDisplayed() {
		try{
			byClassName("answer-form");
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public boolean editedTouchHasImage() {
		WebElement edited = byClassName("post-touchs").findElement(By.cssSelector(".touch.edited-touch"));
		return isElementPresent(tagName("img"), edited);
	}
	
	public List<String> firstAnswerComments() {
		List<WebElement> commentsElements = allByCSS(".answer .comment span p");
		return toListString(commentsElements);
	}
	
	public List<String> questionComments() {
		List<WebElement> commentsElements = allByCSS(".comment-container span");
		return toListString(commentsElements);
	}
	
	private List<String> toListString(List<WebElement> elements) {
		List<String> listString = new ArrayList<>();
		for (WebElement e : elements) {
			listString.add(e.getText());
		}
		return listString;
	}
	
}

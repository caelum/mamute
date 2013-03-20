package br.com.caelum.brutal.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewQuestionPage extends PageObject {
	
	public static final String URL = "/perguntar";
	private WebElement newQuestionForm;

	public NewQuestionPage(WebDriver driver) {
		super(driver);
		newQuestionForm = byClassName("question-form");
	}
	
	public QuestionPage newQuestion(String title, String description, String tags){
		typeTitle(title);
		typeDescription(description);
		typeTags(tags);
		return submit();
	}
	
	public NewQuestionPage typeDescription(String description) {
	    return type("description", description);
	}
	
	public NewQuestionPage typeTitle(String title) {
	    return type("title", title);
	}
	
	private NewQuestionPage type(String fieldName, String content) {
	    newQuestionForm.findElement(By.name(fieldName)).sendKeys(content);
	    return this;
	}

	public NewQuestionPage typeTags(String tags) {
		newQuestionForm.findElement(By.name("tagNames")).sendKeys(tags);
		return this;
	}

	public boolean hasAutoCompleteSuggestion(String tag) throws InterruptedException {
		WebElement autoCompletedTag = findAutoCompletedTag(tag);
		return autoCompletedTag != null;
	}
	
	public QuestionPage submit() {
		newQuestionForm.submit();
		return new QuestionPage(driver);
	}

	public NewQuestionPage selectAutoCompleteSuggestion(String tag) {
		WebElement autoCompletedTag = findAutoCompletedTag(tag);
		autoCompletedTag.click();
		return this;
	}

	private WebElement findAutoCompletedTag(String tag) {
		String tagSelector = ".complete-tag .tag";
        waitForElement(By.cssSelector(tagSelector), 10);
		List<WebElement> autoCompletedTags = allByCSS(tagSelector);
		WebElement rightAutoCompletedTag = null;
		for (WebElement autoCompletedTag : autoCompletedTags) {
			if (autoCompletedTag.getText().equals(tag)){
				rightAutoCompletedTag = autoCompletedTag;
			}
		}
		return rightAutoCompletedTag;
	}

	public boolean hasTag(String tag) {
		return byName("tagNames").getAttribute("value").contains(tag);
	}

    public boolean descriptionHintIsVisible() {
        String display = byId("question-description-hint").getCssValue("display");
        return !display.equals("none");
    }
    
    public boolean titleHintIsVisible() {
        String display = byId("question-title-hint").getCssValue("display");
        return !display.equals("none");
    }
}

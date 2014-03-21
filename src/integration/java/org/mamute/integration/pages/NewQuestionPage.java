package org.mamute.integration.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
		String tagSelector = ".complete-tag .tag-brutal";
        waitForElement(By.cssSelector(tagSelector), 15);
        List<String> tagsFound = new ArrayList<>();
		List<WebElement> autoCompletedTags = allByCSS(tagSelector);
		WebElement rightAutoCompletedTag = null;
		for (WebElement autoCompletedTag : autoCompletedTags) {
			String tagText = autoCompletedTag.getText();
			tagsFound.add(tagText);
			if (tagText.equals(tag)) {
				rightAutoCompletedTag = autoCompletedTag;
			}
		}
		if (rightAutoCompletedTag == null) {
			throw new NoSuchElementException("could not find tag named " + tag + ", tags found: " + tagsFound);
		}
		return rightAutoCompletedTag;
	}

	public boolean hasTag(String tag) {
		return byName("tagNames").getAttribute("value").contains(tag);
	}

    public boolean descriptionHintIsVisible() {
    	String descriptionHintId = "question-description-hint";
    	WebElement hint = byId(descriptionHintId);
		waitForVisibleElement(hint, 5);
		String display = hint.getCssValue("display");
        return !display.equals("none");
    }
    

	public boolean titleHintIsVisible() {
    	String titleHintId = "question-title-hint";
        WebElement hint = byId(titleHintId);
        waitForVisibleElement(hint, 5);
		String display = hint.getCssValue("display");
        return !display.equals("none");
    }
	
	public boolean hasInformation(String title, String description, String tags) {
		return hasTitle(title) && hasDescription(description) && hasTags(tags);
	}

	private boolean hasTitle(String questionTitle) {
		String actual = formField("title").getAttribute("value");
		return questionTitle.equals(actual);
	}
	
	private boolean hasDescription(String questionDescription) {
		WebElement textArea = formField("description");
		String actual = textArea.getText();
		return questionDescription.equals(actual);
	}
	
	private WebElement questionForm() {
		return byClassName("question-form");
	}
	
	private WebElement formField(String name) {
		return questionForm().findElement(By.name(name));
	}
	
	private boolean hasTags(String tags) {
		String actual = formField("tagNames").getAttribute("value");
		return tags.equals(actual.trim());
	}}
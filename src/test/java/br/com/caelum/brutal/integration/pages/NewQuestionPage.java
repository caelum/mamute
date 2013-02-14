package br.com.caelum.brutal.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewQuestionPage extends PageObject {
	
	private WebElement newQuestionForm;

	public NewQuestionPage(WebDriver driver) {
		super(driver);
		newQuestionForm = byClassName("question-form");
	}
	
	public QuestionPage newQuestion(String title, String description, String tags){
		newQuestionForm.findElement(By.name("title")).sendKeys(title);
		newQuestionForm.findElement(By.name("description")).sendKeys(description);
		typeTags(tags);
		newQuestionForm.submit();
		return new QuestionPage(driver);
	}

	public NewQuestionPage typeTags(String tags) {
		newQuestionForm.findElement(By.name("tagNames")).sendKeys(tags);
		return this;
	}

	public boolean hasAutoCompleteSuggestion(String tag) throws InterruptedException {
		WebElement autoCompletedTag = findAutoCompletedTag(tag);
		return autoCompletedTag != null;
	}


	public NewQuestionPage selectAutoCompleteSuggestion(String tag) {
		WebElement autoCompletedTag = findAutoCompletedTag(tag);
		autoCompletedTag.click();
		return this;
	}

	private WebElement findAutoCompletedTag(String tag) {
		waitForElement(By.className("autocompleted-tag"));
		List<WebElement> autoCompletedTags = allByClassName("autocompleted-tag");
		WebElement rightAutoCompletedTag = null;
		for (WebElement autoCompletedTag : autoCompletedTags) {
			if(autoCompletedTag.getText().equals(tag)){
				rightAutoCompletedTag = autoCompletedTag;
			}
		}
		return rightAutoCompletedTag;
	}

	public boolean hasTag(String tag) {
		return byName("tagNames").getAttribute("value").contains(tag);
	}
}

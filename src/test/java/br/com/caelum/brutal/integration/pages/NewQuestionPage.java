package br.com.caelum.brutal.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
		waitForElement(By.className("autocompleted-tag"));
		List<WebElement> autoCompletedTags = allByClassName("autocompleted-tag");
		for (WebElement autoCompletedTag : autoCompletedTags) {
			String mydivText = (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerText;", autoCompletedTag);
			if(autoCompletedTag.getText().equals(tag)) return true;
		}
		return false;
	}

}

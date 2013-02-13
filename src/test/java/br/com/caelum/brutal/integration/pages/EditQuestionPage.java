package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditQuestionPage extends PageObject {

	public EditQuestionPage(WebDriver driver) {
		super(driver);
	}

	public QuestionPage edit(String title, String description, String tags) {
		WebElement newQuestionForm = byClassName("question-form");
		
		WebElement questionTitle = newQuestionForm.findElement(By.name("title"));
		questionTitle.clear();
		questionTitle.sendKeys(title);
		
		WebElement questionDescription = newQuestionForm.findElement(By.name("description"));
		questionDescription.clear();
		questionDescription.sendKeys(description);
		
		WebElement questionTags = newQuestionForm.findElement(By.name("tagNames"));
		questionTags.clear();
		questionTags.sendKeys(tags);
		
		newQuestionForm.submit();
		return new QuestionPage(driver);
	}

}

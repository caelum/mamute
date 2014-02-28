package org.mamute.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditQuestionPage extends PageObject {

	public EditQuestionPage(WebDriver driver) {
		super(driver);
	}

	public QuestionPage edit(String title, String description, String tags) {
		WebElement editQuestionForm = byClassName("question-form");
		
		WebElement questionTitle = editQuestionForm.findElement(By.name("title"));
		questionTitle.clear();
		questionTitle.sendKeys(title);
		
		WebElement questionDescription = editQuestionForm.findElement(By.name("description"));
		questionDescription.clear();
		questionDescription.sendKeys(description);
		
		WebElement questionTags = editQuestionForm.findElement(By.name("tagNames"));
		questionTags.clear();
		questionTags.sendKeys(tags);
		
		WebElement editComment = editQuestionForm.findElement(By.name("comment"));
		editComment.clear();
		editComment.sendKeys("my comment");
		
		editQuestionForm.submit();
		return new QuestionPage(driver);
	}

}

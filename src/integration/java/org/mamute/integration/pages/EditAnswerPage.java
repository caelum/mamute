package org.mamute.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditAnswerPage extends PageObject{

	public EditAnswerPage(WebDriver driver) {
		super(driver);
	}

	public QuestionPage edit(String description, String editComment) {
		WebElement editAnswerForm = byClassName("answer-form");
		
		WebElement descriptionInput = editAnswerForm.findElement(By.name("description"));
		descriptionInput.clear();
		descriptionInput.sendKeys(description);
		
		WebElement commentInput = editAnswerForm.findElement(By.name("comment"));
		commentInput.clear();
		commentInput.sendKeys(editComment);
		
		descriptionInput.submit();
		
		return new QuestionPage(driver);
	}

}

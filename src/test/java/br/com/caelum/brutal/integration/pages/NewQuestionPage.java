package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewQuestionPage extends PageObject {
	
	public NewQuestionPage(WebDriver driver) {
		super(driver);
	}
	
	public QuestionPage newQuestion(String title, String description, String tags){
		WebElement newQuestionForm = byClassName("question-form");
		newQuestionForm.findElement(By.name("title")).sendKeys(title);
		newQuestionForm.findElement(By.name("description")).sendKeys(description);
		newQuestionForm.findElement(By.name("tagNames")).sendKeys(tags);
		newQuestionForm.submit();
		return new QuestionPage(driver);
	}

}

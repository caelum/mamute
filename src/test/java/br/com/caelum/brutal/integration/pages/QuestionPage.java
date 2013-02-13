package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.WebDriver;

public class QuestionPage extends PageObject{

	public QuestionPage(WebDriver driver) {
		super(driver);
	}

	public boolean isTheQuestion(String questionTitle) {
		return questionTitle.equals(byClassName("question-title").getText());
	}

	
}

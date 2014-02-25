package org.mamute.integration.pages;

import static org.openqa.selenium.By.className;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WithTagListPage extends PageObject{

	public WithTagListPage(WebDriver driver) {
		super(driver);
	}

	public boolean hasOnlyQuestionsWithTag(String tag) {
		List<WebElement> questions = byClassName("question-list").findElements(className("question"));
		for (WebElement question : questions) {
			if(!containsTag(question, tag)) return false;
		}
		return true;

	}

	private boolean containsTag(WebElement question, String tag) {
		List<WebElement> questionTags = question.findElements(className("tag"));
		for (WebElement currentTag : questionTags) {
			if(currentTag.getText().equals(tag)){
				return true;
			}
		}
		return false;
	}

}

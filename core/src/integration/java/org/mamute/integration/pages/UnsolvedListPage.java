package org.mamute.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UnsolvedListPage extends PageObject{

	public UnsolvedListPage(WebDriver driver) {
		super(driver);
	}

	public boolean hasOnlyUnsolvedQuestions() {
		List<WebElement> answerCounts = byClassName("question-list").findElements(By.className("answers"));
		if(answerCounts.isEmpty()) return false;
		for (WebElement answerCount : answerCounts) {
			if(answerCount.getAttribute("class").contains("solved")){
				return false;
			}
		}
		return true;
	}

}

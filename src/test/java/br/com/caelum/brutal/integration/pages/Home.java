package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.WebDriver;

public class Home extends PageObject {

	public Home(WebDriver driver) {
		super(driver);
	}

	public boolean containsFirstMessage(String msg) {
		return allByClassName("item-title").get(0).getText().equals(msg);
	}

}

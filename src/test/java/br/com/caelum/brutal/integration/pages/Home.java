package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Home extends PageObject {

	public Home(WebDriver driver) {
		super(driver);
	}

	public boolean containsFirstMessage(String msg) {
		return allByClassName("item-title").get(0).getText().equals(msg);
	}
	
	public LoginPage toLoginPage(){
		allByClassName("login").get(0).click();
		LoginPage loginPage = new LoginPage(driver);
		return loginPage;
	}

	public boolean isLoggedIn() {
		return allByClassName("login").isEmpty();
	}

}

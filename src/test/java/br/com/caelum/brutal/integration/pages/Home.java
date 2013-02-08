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
		driver.findElement(By.className("login")).click();
		LoginPage loginPage = new LoginPage(driver);
		return loginPage;
	}

	public boolean isLoggedIn() {
		return !driver.findElement(By.linkText("/login")).isDisplayed();
	}

}

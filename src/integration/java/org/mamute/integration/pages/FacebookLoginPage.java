package org.mamute.integration.pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FacebookLoginPage extends PageObject {

	private WebElement loginForm;

	public FacebookLoginPage(WebDriver driver) {
		super(driver);
		loginForm = byId("login_form");
	}
	
	public FacebookLoginPage writeEmail(String email) {
		WebElement emailInput = loginForm.findElement(By.name("email"));
		emailInput.sendKeys(email);
		return this;
	}
	
	public FacebookLoginPage writePassword(String pass) {
		WebElement passInput = loginForm.findElement(By.name("pass"));
		passInput.sendKeys(pass);
		return this;
	}

	public FacebookLoginPage submit() {
		loginForm.submit();
		return this;
	}
	
	public Home confirm() {
		if (!driver.findElements(By.name("grant_clicked")).isEmpty()) {
			byName("grant_clicked").click();
		} else if (!driver.findElements(By.name("__CONFIRM__")).isEmpty()) {
			byName("__CONFIRM__").click();
		} else {
			throw new NoSuchElementException("could not find facebook confrmation button, maybe they changed their page?");
		}
		return new Home(driver);
	}

}

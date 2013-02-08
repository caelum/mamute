package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends PageObject{

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public Home login(String email, String password) {
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.tagName("form")).submit();
		return new Home(driver);
	}
	
}

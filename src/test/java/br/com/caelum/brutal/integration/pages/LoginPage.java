package br.com.caelum.brutal.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends PageObject{

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public Home login(String email, String password) {
		WebElement loginForm = allByClassName("user-form").get(0);
		loginForm.findElement(By.name("email")).sendKeys(email);
		loginForm.findElement(By.name("password")).sendKeys(password);
		loginForm.submit();
		return new Home(driver);
	}
	
}

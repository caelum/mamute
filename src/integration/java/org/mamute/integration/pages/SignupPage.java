package org.mamute.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SignupPage extends PageObject{

	private final WebDriver driver;

	public SignupPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}
	
	public Home signUp(String name, String email, String password, String passwordConfirmation){
		WebElement signupForm = allByClassName("user-form").get(0);
		signupForm.findElement(By.name("name")).sendKeys(name);
		signupForm.findElement(By.name("email")).sendKeys(email);
		signupForm.findElement(By.name("password")).sendKeys(password);
		signupForm.findElement(By.name("passwordConfirmation")).sendKeys(passwordConfirmation);
		signupForm.submit();
		return new Home(driver);
	}

}

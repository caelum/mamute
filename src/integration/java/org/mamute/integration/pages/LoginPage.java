package org.mamute.integration.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends PageObject {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public Home login(String email, String password) {
		WebElement loginForm = byClassName("user-form");
		loginForm.findElement(By.name("email")).sendKeys(email);
		loginForm.findElement(By.name("password")).sendKeys(password);
		loginForm.submit();
		return new Home(driver);
	}

	public boolean hasRedirectUrl(String url) {
		return byName("redirectUrl").getAttribute("value").contains(url);
	}

    public ForgotPasswordPage toForgotPasswordPage() {
        byClassName("forgot-password").click();
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        return forgotPasswordPage;
    }

	public Home loginThroughFacebook() {
		byClassName("face-button").click();
		return new Home(driver);
	}

	public FacebookLoginPage signupWithFacebook() {
		WebElement faceButton = byClassName("face-button");
		faceButton.click();
		return new FacebookLoginPage(driver);
	}

	public Home signUp(String name, String email, String password) {
		WebElement signupForm = byCSS(".signup form");
		signupForm.findElement(By.name("name")).sendKeys(name);
		signupForm.findElement(By.name("email")).sendKeys(email);
		signupForm.findElement(By.name("password")).sendKeys(password);
		signupForm.findElement(By.name("passwordConfirmation")).sendKeys(password);
		signupForm.submit();
		return new Home(driver);
	}
	
}

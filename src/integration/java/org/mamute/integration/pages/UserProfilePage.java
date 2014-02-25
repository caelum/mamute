package org.mamute.integration.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserProfilePage extends PageObject {

	private final WebDriver driver;

	public UserProfilePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String userEmail() {
		WebElement profileEmail = byClassName("profile-email");
		return profileEmail.getText();
	}

}

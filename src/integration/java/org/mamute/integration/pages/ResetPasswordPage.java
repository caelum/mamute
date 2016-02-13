package org.mamute.integration.pages;

import org.openqa.selenium.WebDriver;

public class ResetPasswordPage extends PageObject {

    public ResetPasswordPage(WebDriver driver, String url) {
        super(driver);
        driver.get(url);
    }

    public ResetPasswordPage typePassword(String password) {
        byName("password").sendKeys(password);
        byName("passwordConfirmation").sendKeys(password);
        return this;
    }

    public void submitNewPassword() {
        byClassName("user-form").submit();
    }
    
    
}

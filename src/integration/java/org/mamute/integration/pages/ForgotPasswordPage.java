package org.mamute.integration.pages;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ForgotPasswordPage extends PageObject {

    private static String FORM_CLASS = "user-form";
    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }
    
    public ForgotPasswordPage typeEmail(String email) {
        byName("email").sendKeys(email);
        return this;
    }
    
    public ForgotPasswordPage sendResetPassword() {
        WebElement emailForm = byClassName(FORM_CLASS);
        emailForm.submit();
        return this;
    }
    
    public boolean emailWasSent() {
        try {
        	List<WebElement> confirmations = allByClassName("confirmation");
        	return !confirmations.isEmpty();
        } catch (NoSuchElementException e) {
            return true;
        }
    }

}

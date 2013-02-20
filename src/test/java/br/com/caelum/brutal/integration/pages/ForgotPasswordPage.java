package br.com.caelum.brutal.integration.pages;

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
    
    public ForgotPasswordPage resetPassword() {
        WebElement emailForm = byClassName(FORM_CLASS);
        emailForm.submit();
        return this;
    }
    
    public boolean emailWasSent() {
        try {
            byClassName(FORM_CLASS);
        } catch (NoSuchElementException e) {
            return true;
        }
        return false;
    }

}

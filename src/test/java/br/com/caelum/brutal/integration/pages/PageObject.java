package br.com.caelum.brutal.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageObject {

	protected final WebDriver driver;

	public PageObject(WebDriver driver) {
		this.driver = driver;
	}
	
	protected WebElement byId(String id) {
		return driver.findElement(By.id(id));
	}

	protected WebElement byName(String name) {
		return driver.findElement(By.name(name));
	}

	protected List<WebElement> allByClassName(String name) {
		return driver.findElements(By.className(name));
	}
	
	protected WebElement byClassName(String name) {
		return driver.findElement(By.className(name));
	}
	
	protected void waitForElement(final By by) {
		ExpectedCondition<WebElement> elementAppear = new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(by);
			}
		};
		new WebDriverWait(driver, 40).until(elementAppear);
	}
}

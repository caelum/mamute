package br.com.caelum.brutal.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
}

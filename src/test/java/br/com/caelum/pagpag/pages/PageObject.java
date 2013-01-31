package br.com.caelum.pagpag.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public abstract class PageObject {

	protected final WebDriver driver;

	public PageObject(WebDriver driver) {
		this.driver = driver;
	}
	
	protected void preencher(WebElement input,
			String valor, boolean clickBefore) {
		if(valor != null) {
			input.clear();
			input.click();
			input.sendKeys(valor);
		}
	}

	protected void preencher(WebElement input, String valor) {
		preencher(input, valor, false);
	}
	
	protected void selecionar(Select select, String valor) {
		if(valor != null) {
			select.selectByValue(valor);
		}
	}

	protected void preencher(Select select, String valor) {
		if(valor != null){
			select.selectByValue(valor);
		}
	}
	
	protected WebElement byId(String id) {
		return driver.findElement(By.id(id));
	}

	protected WebElement byName(String name) {
		return driver.findElement(By.name(name));
	}

}

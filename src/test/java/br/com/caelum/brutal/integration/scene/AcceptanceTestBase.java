package br.com.caelum.brutal.integration.scene;

import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.caelum.brutal.integration.pages.Home;
import br.com.caelum.pagpag.aceitacao.util.ServerInfo;

public abstract class AcceptanceTestBase implements ServerInfo.TesteAceitacao {

	protected static WebDriver driver;

	protected HttpClient client;

	@AfterClass
	public static void close() {
		if (driver != null)
			driver.close();
	}

	private static WebDriver htmlUnitDriver() {
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		return driver;
	}

	protected Navigation browser() {
		return driver.navigate();
	}
	
	protected Home brutal() {
		return new Home(driver);
	}

	@Before
	public void setUpEnv() {

		driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
		client = new HttpClient();
	}

	@BeforeClass
	public static void getDriver() {
		if ("htmlunit".equals(System.getProperty("vraptor.browser"))) {
			driver = htmlUnitDriver();
		} else {
			driver = new FirefoxDriver();
		}
		waitForFirstBodyPresence();
	}

	private static void waitForFirstBodyPresence() {
		driver.get(SERVER.urlFor(""));
		ExpectedCondition<WebElement> homeAppear = new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(By.tagName("body"));
			}
		};
		new WebDriverWait(driver, 40).until(homeAppear);
	}

}

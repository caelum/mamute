package br.com.caelum.brutal.integration.scene;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
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
import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public abstract class AcceptanceTestBase implements ServerInfo.TesteAceitacao {

	protected static WebDriver driver;

	protected static HttpClient client;
	
	protected static Environment env;

	@AfterClass
	public static void close() {
		if (driver != null)
			driver.close();
	}

	private static WebDriver htmlUnitDriver() {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6);
		driver.setJavascriptEnabled(true);
		return driver;
	}

	protected Navigation browser() {
		return driver.navigate();
	}
	
	protected Home home() {
		return new Home(driver);
	}

	@Before
	public void setUpEnv() {
//		waitForFirstBodyPresence();
		client = new HttpClient();
	}
	

	@BeforeClass
	public static void getDriver() {
		if ("firefox".equals(System.getProperty("vraptor.browser"))) {
			driver = new FirefoxDriver();
		} else {
			driver = htmlUnitDriver();
		}
		waitForFirstBodyPresence();
	}
	
	@BeforeClass
	public static void getHttpClient() {
	    client = new HttpClient();
	    getHome();
	}
	
	@BeforeClass
	public static void getEnv() throws IOException {
	    String homologEnv = System.getenv("ACCEPTANCE_ENV");
        if (homologEnv == null) {
            homologEnv = "development";
        }
        env = new DefaultEnvironment(homologEnv);
	}

	private static void getHome() {
	    String homeUri = SERVER.urlFor("");
        try {
            HttpMethod method = new GetMethod(homeUri);
            int status = client.executeMethod(method);
            int digit = status % 100;
            if (digit == 5 || digit == 4) {
                throw new RuntimeException("server responded with "+ status + " status for a GET request to uri: " + homeUri + ", is the server ok?");
            }
        } catch (IOException e) {
            throw new RuntimeException("could not execute GET to: " + homeUri + ", is the server up?", e);
        }
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

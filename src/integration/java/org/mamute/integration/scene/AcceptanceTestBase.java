package org.mamute.integration.scene;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mamute.integration.pages.Home;
import org.mamute.integration.pages.QuestionPage;
import org.mamute.integration.util.AppMessages;
import org.mamute.integration.util.ServerInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.EnvironmentType;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public abstract class AcceptanceTestBase implements ServerInfo.AcceptanceTest {

	private static final int TIME_WAIT = 5;

	protected static WebDriver driver;

	protected static HttpClient client;

	protected static Environment env;

	private AppMessages messages = new AppMessages();

	@AfterClass
	public static void close() {
		if (driver != null)
			driver.close();
	}

	@SuppressWarnings("unused")
	private static WebDriver htmlUnitDriver() {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
		driver.setJavascriptEnabled(true);
		return driver;
	}

	private static WebDriver ghostDriver() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
		try {
			return new RemoteWebDriver(new URL("http://localhost:8787/"),
					capabilities);
		} catch (MalformedURLException e) {
			throw new RuntimeException("could not build ghost driver", e);
		}
	}

	protected Navigation browser() {
		return driver.navigate();
	}

	protected Home home() {
		return new Home(driver);
	}

	@Before
	public void setUpEnv() {
		client = new HttpClient();
	}

	@Before
	public void setUpImplicitWait() {
		driver.manage().timeouts()
				.implicitlyWait(implicitWaitSeconds(), TimeUnit.SECONDS);
	}

	@BeforeClass
	public static void buildDriver() {
//		System.setProperty("webdriver.chrome.driver",
//				"/home/csokol/programas/chromedriver/chromedriver");
//		driver = new ChromeDriver();
		String localTest = System.getenv("LOCAL_TEST");
		if ("remote".equals(localTest)) {
			driver = ghostDriver();
		} else {
			FirefoxBinary firefox = new FirefoxBinary();
			String display = System.getProperty("DISPLAY", ":0");
			firefox.setEnvironmentProperty("DISPLAY", display);
			driver = new FirefoxDriver();
		}
		driver.manage().window().setSize(new Dimension(1280, 800));
		waitForFirstBodyPresence();
	}

	public static WebDriver getDriver() {
		return driver;
	}

	@BeforeClass
	public static void getHttpClient() {
		client = new HttpClient();
		String homeUri = SERVER.urlFor("");
		try {
			HttpMethod method = new GetMethod(homeUri);
			int status = client.executeMethod(method);
			int digit = status % 100;
			if (digit == 5 || digit == 4) {
				throw new RuntimeException("server responded with " + status
						+ " status for a GET request to uri: " + homeUri
						+ ", is the server ok?");
			}
		} catch (IOException e) {
			throw new RuntimeException("could not execute GET to: " + homeUri
					+ ", is the server up?", e);
		}
	}

	@BeforeClass
	public static void getEnv() throws IOException {
		String homologEnv = System.getenv("ACCEPTANCE_ENV");
		if (homologEnv == null) {
			homologEnv = "development";
		}
		env = new DefaultEnvironment(new EnvironmentType(homologEnv));
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

	protected String message(String text) {
		return messages.getMessage(text);
	}

	protected QuestionPage createQuestion() {
		return home()
				.toNewQuestionPage()
				.newQuestion(
						"question title question title question title",
						"question description question description question description question description ",
						"java");
	}

	protected void removeBindsFromElement(By by) {
		WebElement element = driver.findElement(by);
		if (driver instanceof JavascriptExecutor) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(getScript("/remove-binds.js"), element);
		}
	}

	private String getScript(String file) {
		String script;
		try {
			InputStream is = AcceptanceTestBase.class.getResourceAsStream(file);
			script = IOUtils.toString(is);
			return script;
		} catch (IOException e) {
			throw new RuntimeException("You need to create the file: '" + file
					+ "' at src/integration/resources");
		}
	}

	public int implicitWaitSeconds() {
		return TIME_WAIT;
	}

}

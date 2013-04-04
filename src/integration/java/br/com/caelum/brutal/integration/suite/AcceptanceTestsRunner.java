package br.com.caelum.brutal.integration.suite;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.InitializationError;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.com.caelum.brutal.integration.scene.AnonymousTest;
import br.com.caelum.brutal.integration.scene.AuthTest;
import br.com.caelum.brutal.integration.scene.CommentQuestionTest;
import br.com.caelum.brutal.integration.scene.CreateQuestionTest;
import br.com.caelum.brutal.integration.scene.EditAnswerTest;
import br.com.caelum.brutal.integration.scene.EditQuestionTest;
import br.com.caelum.brutal.integration.scene.ForgotPasswordTest;
import br.com.caelum.brutal.integration.scene.ListTest;
import br.com.caelum.brutal.integration.scene.SignupTest;
import br.com.caelum.brutal.integration.scene.VoteUpDownTest;


@SuiteClasses({
	AnonymousTest.class, 
	AuthTest.class, 
	CommentQuestionTest.class,
	CreateQuestionTest.class,
	EditAnswerTest.class,
	EditQuestionTest.class,
	ForgotPasswordTest.class,
	ListTest.class,
	SignupTest.class,
	VoteUpDownTest.class
})
public class AcceptanceTestsRunner extends Runner {

	private static WebDriver DRIVER = null;
	private static boolean RUNNING = false;
	private final BlockJUnit4ClassRunner delegate;
	
	public AcceptanceTestsRunner(Class<?> klass) throws InitializationError  {
		delegate = new BlockJUnit4ClassRunner(klass);
	}
	
	
	@BeforeClass 
	public static void setup() throws MalformedURLException {
		RUNNING = true;
//		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//		desiredCapabilities.setJavascriptEnabled(true);
//		DRIVER = new RemoteWebDriver(new URL("http://localhost:8787"), desiredCapabilities);
		DRIVER = new FirefoxDriver();
	}
	
	@AfterClass 
	public static void tearDown() {
		DRIVER.close();
		RUNNING = false;
		DRIVER = null;
	}

	public static boolean isRunning() {
		return RUNNING;
	}
	
	public static WebDriver getDriver() {
		if (!RUNNING) {
			throw new IllegalStateException("you may get the driver only we're running the tests");
		}
		return DRIVER;
	}


	@Override
	public Description getDescription() {
		return delegate.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		delegate.run(notifier);
	}


}

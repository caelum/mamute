package br.com.caelum.brutal.integration.suite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
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
@RunWith(Suite.class)
public class AcceptanceTestsSuite {
	
	private static WebDriver DRIVER = null;
	private static boolean RUNNING = false;

	@BeforeClass 
	public static void setup() {
		RUNNING = true;
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
	
}

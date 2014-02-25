package org.mamute.integration.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.mamute.integration.scene.AcceptanceTestBase;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class FailFastListener extends RunListener {

	private WebDriver driver;

	public void testFailure(Failure failure) throws Exception {
		driver = AcceptanceTestBase.getDriver();
		String failFast = System.getenv("FAIL_FAST");
		saveHtmlAndScreenshot(failure);
		if ("true".equals(failFast)) {
			System.err.println("FAILURE: " + failure);
			failure.getException().printStackTrace();
			Boolean closeOnExit = System.getenv("CLOSE_DRIVER_ON_EXIT") != null;
			if (closeOnExit) {
				AcceptanceTestBase.close();
			}
			System.exit(-1);
		}
	}

	private void saveHtmlAndScreenshot(Failure failure) throws IOException {
		File tempDir = new File("target/tmp/output/");
		tempDir.mkdirs();
		saveHtml(failure, tempDir);
		takeScreenshot(tempDir);
	}

	private void takeScreenshot(File tempDir) throws IOException {
		try {
			driver = new Augmenter().augment(driver);
	        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File png = new File(tempDir, "failure" + System.currentTimeMillis() + ".png");
			FileUtils.copyFile(srcFile, png);
			System.err.println("screenshot saved to " + png.getAbsolutePath());
		} catch (Exception e) {
			System.err.println("could not save screen shot, see exception below");
			e.printStackTrace();
		}
	}

	private void saveHtml(Failure failure, File tempDir)
			throws IOException {
		String pageSource = driver.getPageSource();
		File html = new File(tempDir, "failure" + System.currentTimeMillis() + ".html");
		FileUtils.writeStringToFile(html , pageSource);
		System.err.println("FAILURE: " + failure);
		System.err.println("html output written to " + html.getAbsolutePath());
	}

}

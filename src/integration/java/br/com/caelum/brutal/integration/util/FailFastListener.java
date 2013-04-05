package br.com.caelum.brutal.integration.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.WebDriver;

import br.com.caelum.brutal.integration.scene.AcceptanceTestBase;

public class FailFastListener extends RunListener {
	
	public void testFailure(Failure failure) throws Exception {
		System.err.println("FAILURE: " + failure);
		String getenv = System.getenv("CLOSE_DRIVER_ON_EXIT");
		WebDriver driver = AcceptanceTestBase.getDriver();
		String pageSource = driver.getPageSource();
		File file = new File("/tmp/error.html");
		System.err.println("page source written to " + file.getAbsolutePath());
		FileUtils.writeStringToFile(file, pageSource);
		if (getenv != null) {
			AcceptanceTestBase.close();
		}
		System.exit(-1);
	}
	
}

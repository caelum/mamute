package br.com.caelum.brutal.integration.util;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import br.com.caelum.brutal.integration.scene.AcceptanceTestBase;

public class FailFastListener extends RunListener {

	public void testFailure(Failure failure) throws Exception {
		String failFast = System.getenv("FAIL_FAST");
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

}

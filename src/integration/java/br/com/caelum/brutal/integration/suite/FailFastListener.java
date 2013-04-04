package br.com.caelum.brutal.integration.suite;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class FailFastListener extends RunListener {

	private RunNotifier runNotifier;

	public FailFastListener(RunNotifier runNotifier) {
		super();
		this.runNotifier = runNotifier;
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		this.runNotifier.pleaseStop();
	}
}
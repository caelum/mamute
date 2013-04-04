package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.caelum.brutal.integration.pages.Home;
import br.com.caelum.brutal.integration.suite.AcceptanceTestsRunner;

@RunWith(AcceptanceTestsRunner.class)
public class AnonymousTest extends AcceptanceTestBase {
	Home home = home();

	@Test
	public void should_be_able_to_enter_the_homepage() {
		assertTrue(home.isLoadedCorrectly());
	}
}

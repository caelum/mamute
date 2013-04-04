package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.integration.pages.Home;

public class AnonymousTest extends AcceptanceTestBase {
	Home home = home();

	@Test
	public void should_be_able_to_enter_the_homepage() {
		assertTrue(home.isLoadedCorrectly());
	}
}

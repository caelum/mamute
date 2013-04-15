package br.com.caelum.brutal.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModerationOptionsTest {
	private ModerationOptions moderationOptions = new ModerationOptions();

	@Test
	public void should_become_invisible() {
		assertFalse(moderationOptions.isInvisible());
		moderationOptions.checkVisibility(ModerationOptions.VISIBILITY_THRESHOLD);
		assertTrue(moderationOptions.isInvisible());
	}
	
	@Test
	public void should_become_visible() {
		assertFalse(moderationOptions.isInvisible());
		moderationOptions.checkVisibility(ModerationOptions.VISIBILITY_THRESHOLD);
		moderationOptions.checkVisibility(ModerationOptions.VISIBILITY_THRESHOLD + 1);
		assertFalse(moderationOptions.isInvisible());
	}

}

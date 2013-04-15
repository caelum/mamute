package br.com.caelum.brutal.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModerationOptionsTest {
	private ModerationOptions moderationOptions = new ModerationOptions();

	@Test
	public void should_become_invisible() {
		assertFalse(moderationOptions.isInvisible());
		moderationOptions.checkVisibility(ModerationOptions.VISIBILITY_THRESHOLD - 1);
		assertTrue(moderationOptions.isInvisible());
	}
	
	@Test
	public void should_become_visible() {
		assertFalse(moderationOptions.isInvisible());
		moderationOptions.checkVisibility(ModerationOptions.VISIBILITY_THRESHOLD - 1);
		moderationOptions.checkVisibility(ModerationOptions.VISIBILITY_THRESHOLD);
		assertFalse(moderationOptions.isInvisible());
	}

}

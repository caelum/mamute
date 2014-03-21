package org.mamute.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mamute.auth.rules.ComposedRule;
import org.mamute.auth.rules.PermissionRule;
import org.mamute.model.User;

public class ComposedRuleTest {
	private True TRUE = new True();
	private False FALSE = new False();

	@Test
	public void should_use_or() {
		assertTrue(composedRule(TRUE).or(FALSE).isAllowed(null, null));
		assertTrue(composedRule(TRUE).or(TRUE).isAllowed(null, null));
		assertTrue(composedRule(FALSE).or(TRUE).isAllowed(null, null));
		assertFalse(composedRule(FALSE).or(FALSE).isAllowed(null, null));
	}

	@Test
	public void multiple_ors_should_work() {
		assertTrue(composedRule(FALSE).or(FALSE).or(TRUE).isAllowed(null, null));
		assertTrue(composedRule(FALSE).or(TRUE).or(FALSE).isAllowed(null, null));
		assertTrue(composedRule(TRUE).or(FALSE).or(FALSE).isAllowed(null, null));
		assertFalse(composedRule(FALSE).or(FALSE).or(FALSE).isAllowed(null, null));
	}
	
	@Test
	public void should_use_and() {
		assertFalse(composedRule(TRUE).and(FALSE).isAllowed(null, null));
		assertTrue(composedRule(TRUE).and(TRUE).isAllowed(null, null));
		assertFalse(composedRule(FALSE).and(TRUE).isAllowed(null, null));
		assertFalse(composedRule(FALSE).and(FALSE).isAllowed(null, null));
	}
	
	@Test
	public void mixed_rules_should_work() {
		assertFalse(composedRule(FALSE).and(FALSE).and(FALSE).isAllowed(null, null));
		assertTrue(composedRule(FALSE).and(FALSE).or(TRUE).isAllowed(null, null));
	}
	
	private ComposedRule<Void> composedRule(PermissionRule<Void> first) {
		return new ComposedRule<Void>(first);
	}
	
	static private class True implements PermissionRule<Void> {
		@Override
		public boolean isAllowed(User u, Void item) {
			return true;
		}
	}
	
	static private class False implements PermissionRule<Void> {
		@Override
		public boolean isAllowed(User u, Void item) {
			return false;
		}
	}

}

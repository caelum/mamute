package org.mamute.model.ban;

import org.junit.Test;

import static org.junit.Assert.*;

public class IpMatcherTest {

	@Test
	public void shouldMatchSimpleIp() {
		IpMatcher matcher = new IpMatcher("127.0.0.1");
		assertTrue(matcher.matches("127.0.0.1"));
		assertFalse(matcher.matches("127.0.0.2"));
	}

	@Test
	public void shouldMatchExpressions() {
		IpMatcher matcher = new IpMatcher("127.0.*.1");
		assertTrue(matcher.matches("127.0.1.1"));
		assertTrue(matcher.matches("127.0.2.1"));
		assertFalse(matcher.matches("127.0.0.2"));

		matcher = new IpMatcher("127.0.*.*");
		assertTrue(matcher.matches("127.0.1.1"));
		assertTrue(matcher.matches("127.0.2.2"));
		assertFalse(matcher.matches("127.1.2.2"));
	}
}
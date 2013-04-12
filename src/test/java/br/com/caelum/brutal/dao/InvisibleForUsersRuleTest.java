package br.com.caelum.brutal.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class InvisibleForUsersRuleTest extends TestCase{

	@Test
	public void should_return_filter_if_user_is_not_moderator() {
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user("leonardo", "leo@leo.com"));
		assertTrue(invisibleForUsersRule.getIsInvisibleOrNotFilter("").length()>0);
	}
	
	@Test
	public void should_not_return_filter_if_user_is_moderator() {
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user("leonardo", "leo@leo.com").asModerator());
		assertFalse(invisibleForUsersRule.getIsInvisibleOrNotFilter("").length()>0);
	}
	
	@Test
	public void should_return_filter_if_user_is_null() {
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(null);
		assertTrue(invisibleForUsersRule.getIsInvisibleOrNotFilter("").length()>0);
	}

}

package br.com.caelum.brutal.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.brutal.model.LoggedUser;

public class InvisibleForUsersRuleTest extends TestCase{

	@Test
	public void should_return_filter_if_user_is_not_moderator() {
		LoggedUser user = new LoggedUser(user("leonardo", "leo@leo.com"), null);
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user);
		assertTrue(invisibleForUsersRule.getInvisibleOrNotFilter("").length()>0);
	}
	
	@Test
	public void should_not_return_filter_if_user_is_moderator() {
		LoggedUser user = new LoggedUser(user("leonardo", "leo@leo.com").asModerator(), null);
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user);
		assertFalse(invisibleForUsersRule.getInvisibleOrNotFilter("").length()>0);
	}
	
	@Test
	public void should_return_filter_if_user_is_not_logged_in() {
		LoggedUser user = new LoggedUser(null, null);
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user);
		assertTrue(invisibleForUsersRule.getInvisibleOrNotFilter("").length()>0);
	}

}

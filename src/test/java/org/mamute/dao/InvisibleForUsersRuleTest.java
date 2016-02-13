package org.mamute.dao;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.model.LoggedUser;
import org.mockito.Mockito;

public class InvisibleForUsersRuleTest extends TestCase{

	private Criteria criteria;

	@Before
	public void set_up(){
		criteria = Mockito.mock(Criteria.class);
		when(criteria.add(Mockito.any(Criterion.class))).thenReturn(criteria);
	}
	
	@Test
	public void should_add_filter_if_user_is_not_moderator() {
		LoggedUser user = new LoggedUser(user("leonardo", "leo@leo.com"), null);
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user);
		
		invisibleForUsersRule.addFilter("x", criteria);
		
		verify(criteria, only()).add(Mockito.any(Criterion.class));
	}
	
	@Test
	public void should_not_add_filter_if_user_is_moderator() {
		LoggedUser user = new LoggedUser(user("leonardo", "leo@leo.com").asModerator(), null);
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user);

		invisibleForUsersRule.addFilter("x", criteria);
		
		verify(criteria, never()).add(Mockito.any(Criterion.class));
	}
	
	@Test
	public void should_add_filter_if_user_is_not_logged_in() {
		LoggedUser user = new LoggedUser(null, null);
		InvisibleForUsersRule invisibleForUsersRule = new InvisibleForUsersRule(user);
		
		invisibleForUsersRule.addFilter("x", criteria);
		
		verify(criteria, only()).add(Mockito.any(Criterion.class));
	}

}

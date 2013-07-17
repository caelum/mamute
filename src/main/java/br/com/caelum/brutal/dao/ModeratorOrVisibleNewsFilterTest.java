package br.com.caelum.brutal.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.caelum.brutal.model.LoggedUser;

public class ModeratorOrVisibleNewsFilterTest extends TestCase{

	private Criteria criteria;

	@Before
	public void set_up(){
		criteria = Mockito.mock(Criteria.class);
		when(criteria.add(Mockito.any(Criterion.class))).thenReturn(criteria);
	}
	
	@Test
	public void should_not_add_filter_if_user_is_moderator() {
		LoggedUser user = new LoggedUser(user("leonardo", "leo@leo.com").asModerator(), null);
		ModeratorOrVisibleNewsFilter moderatorOrVisible = new ModeratorOrVisibleNewsFilter(user, null);

		moderatorOrVisible.addFilter("x", criteria);
		
		verify(criteria, never()).add(Mockito.any(Criterion.class));
	}
}

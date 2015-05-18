package org.mamute.list;

import br.com.caelum.vraptor.http.route.Router;
import org.junit.Before;
import org.junit.Test;
import org.mamute.controllers.ListController;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TabsHelperTest {

	private Router router;

	@Before
	public void setup() {
		router = mock(Router.class);
	}

	@Test
	public void tabForType_creates_tab_with_correct_values() throws NoSuchMethodException {
		when(this.router.urlFor(ListController.class, ListController.class.getMethod("topAnswered"))).thenReturn("/answer-link");
		TabsHelper subject = new TabsHelper(this.router);

		Tab tab = subject.tabForType(Tab.Type.ANSWERED);

		assertEquals(tab.getLink(), "/answer-link");
		assertEquals(tab.getLocalizationKey(), "menu.top.answered");
		assertEquals(tab.getType(), Tab.Type.ANSWERED);
	}

	@Test
	public void getTabs_returns_all_tabs_in_correct_order() throws NoSuchMethodException {
		when(this.router.urlFor(ListController.class, ListController.class.getMethod("topAnswered"))).thenReturn("/answer-link");
		when(this.router.urlFor(ListController.class, ListController.class.getMethod("topViewed"))).thenReturn("/viewed-link");
		when(this.router.urlFor(ListController.class, ListController.class.getMethod("topVoted"))).thenReturn("/voted-link");
		TabsHelper subject = new TabsHelper(this.router);

		List<Tab> tabs = subject.getTabs();

		assertEquals(tabs.size(), 3);
		assertEquals(tabs.get(0).getType(), Tab.Type.VOTED);
		assertEquals(tabs.get(1).getType(), Tab.Type.ANSWERED);
		assertEquals(tabs.get(2).getType(), Tab.Type.VIEWED);
	}
}
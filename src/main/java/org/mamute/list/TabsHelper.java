package org.mamute.list;

import br.com.caelum.vraptor.http.route.Router;
import net.vidageek.mirror.dsl.Mirror;
import org.apache.commons.lang.WordUtils;
import org.mamute.controllers.ListController;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class TabsHelper {

	private final Router router;

	@Inject
	public TabsHelper(Router router) {
		this.router = router;
	}

	public Tab tabForType(Tab.Type tabType) {
		String localizationKey = "menu.top." + tabType.getStringValue();
		String methodName = "top" + WordUtils.capitalize(tabType.getStringValue());
		return new Tab(tabType, localizationKey, urlForMethod(methodName));
	}

	public ArrayList<Tab> getTabs()	{
		ArrayList<Tab> tabs = new ArrayList<Tab>();

		for (Tab.Type tabType : asList(Tab.Type.values())) {
			tabs.add(tabForType(tabType));
		}

		return tabs;
	}

	private String urlForMethod(String method) {
		return this.router.urlFor(ListController.class, method(ListController.class, method));
	}

	private Method method(Class<?> clazz, String method) {
		return new Mirror().on(clazz).reflect().method(method).withAnyArgs();
	}
}

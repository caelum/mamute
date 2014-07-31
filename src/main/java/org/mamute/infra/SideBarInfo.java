package org.mamute.infra;

import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.MethodInfo;
import org.apache.commons.httpclient.ConnectMethod;
import org.mamute.components.RecentTagsContainer;
import org.mamute.dao.NewsDAO;
import org.mamute.infra.rss.read.FeedsMap;
import org.mamute.infra.rss.read.RSSFeed;

import br.com.caelum.vraptor.Result;
import org.mamute.stream.Streamed;

public class SideBarInfo {

	@Inject private NewsDAO newses;
	@Inject private Result result;
	@Inject private RecentTagsContainer tagsContainer;
	@Inject private FeedsMap feedsMap;
	@Inject private ControllerMethod method;
	@Inject private HttpServletRequest request;

	public void include(){

		boolean resourceFound = method == null;
		if (resourceFound || method.containsAnnotation(Streamed.class)) {
			return;
		}

		result.include("sidebarNews", newses.allVisibleAndApproved(5));
		result.include("recentTags", tagsContainer.getRecentTagsUsage());
		Set<Entry<String, RSSFeed>> entrySet = feedsMap.entrySet();
		for (Entry<String, RSSFeed> entry : entrySet) {
			result.include(entry.getKey(), entry.getValue());
		}
	}
}

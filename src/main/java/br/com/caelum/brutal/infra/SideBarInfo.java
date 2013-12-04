package br.com.caelum.brutal.infra;

import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.infra.rss.read.FeedsMap;
import br.com.caelum.brutal.infra.rss.read.RSSFeed;
import br.com.caelum.vraptor.Result;

public class SideBarInfo {

	@Inject private NewsDAO newses;
	@Inject private Result result;
	@Inject private RecentTagsContainer tagsContainer;
	@Inject private FeedsMap feedsMap;

	public void include(){
		result.include("sidebarNews", newses.allVisibleAndApproved(5));
		result.include("recentTags", tagsContainer.getRecentTagsUsage());
		Set<Entry<String, RSSFeed>> entrySet = feedsMap.entrySet();
		for (Entry<String, RSSFeed> entry : entrySet) {
			result.include(entry.getKey(), entry.getValue());
		}
	}
}

package br.com.caelum.brutal.infra;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.infra.rss.read.FeedsMap;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class SideBarInfo {

	private final NewsDAO newses;
	private final Result result;
	private final RecentTagsContainer tagsContainer;
	private final FeedsMap feedsMap;

	public SideBarInfo(NewsDAO newsDAO, RecentTagsContainer container, Result result, FeedsMap feedsMap) {
		this.newses = newsDAO;
		this.result = result;
		this.tagsContainer = container;
		this.feedsMap = feedsMap;
	}
	
	public void include(){
		result.include("sidebarNews", newses.allVisibleAndApproved(5));
		result.include("recentTags", tagsContainer.getRecentTagsUsage());
		result.include("jobs", feedsMap.get("jobs"));
	}
}

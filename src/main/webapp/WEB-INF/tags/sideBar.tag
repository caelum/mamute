<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="relatedQuestions" type="java.util.List" required="false"%>

<aside class="sidebar">
	<tags:homeNewsList newses="${sidebarNews}" />
	<tags:brutal-include value="sideBarAd" />
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
	<tags:feed rssUrl="${env.get('jobs.url')}" rssFeed="${jobs}" rssType="jobs"/>
	<tags:feed rssUrl="${env.get('infoq.url')}" rssFeed="${infoq}" rssType="infoq"/>
	<c:if test="${relatedQuestions != null }">
		<tags:relatedQuestions questions="${relatedQuestions}"/>
	</c:if>
</aside>

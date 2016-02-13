<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="tagRankLastMonth" type="java.util.List" required="true" %>
<%@attribute name="tagRankAllTime" type="java.util.List" required="true" %>
<%@attribute name="title" type="java.lang.String" required="true" %>
<%@attribute name="isAskers" required="true" %>

<h2 class="title big-text-title">${title}</h2>
<div class="tag-ranking-section">
<h2 class="title page-title">${t['users.ranking.tag.month']}</h2>
	<c:forEach items="${tagRankLastMonth}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count}<tags:pluralize key="${isAskers ? 'user_profile.questions' : 'user_profile.answers'}" count="${summary.count}" /></span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>
<div class="tag-ranking-section">
<h2 class="title page-title">${t['users.ranking.tag.all_time']}</h2>
	<c:forEach items="${tagRankAllTime}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count}<tags:pluralize key="${isAskers ? 'user_profile.questions' : 'user_profile.answers'}" count="${summary.count}" /></span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>
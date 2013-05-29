<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="tagRank" type="java.util.List" required="true" %>
<%@attribute name="title" type="java.lang.String" required="true" %>
<%@attribute name="isAskers" required="true" %>

<div class="tag-ranking-section">
	<c:forEach items="${tagRank}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count}<tags:pluralize key="${isAskers ? 'user_profile.questions' : 'user_profile.answers'}" count="${summary.count}" /></span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>
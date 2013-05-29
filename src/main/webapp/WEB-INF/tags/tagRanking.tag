<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="tagRank" type="java.util.List" required="true" %>

<div class="tag-ranking-section">
	<c:forEach items="${tagRank}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count} respostas</span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>
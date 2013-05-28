<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tags:header facebookMetas="${true}" title="Ranking de Usuários: [${tag.name}]" />

<h2 class="title page-title subheader">Ranking de Usuários: [${tag.name}]</h2>

<div class="tag-ranking-section">
	<c:forEach items="${answerersLastMonth}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count} respostas</span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>

<div class="tag-ranking-section">
	<c:forEach items="${answerersAllTime}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count} respostas</span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>

<div class="tag-ranking-section">
	<c:forEach items="${askersLastMonth}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count} perguntas</span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>

<div class="tag-ranking-section">
	<c:forEach items="${askersAllTime}" var="summary">
		<span class="tag-ranking-karma karma">${summary.karmaReward}</span><span class="tag-ranking-count">${summary.count} perguntas</span><tags:RankingUser user="${summary.user}" isTagRanking="true"/>
	</c:forEach>
</div>
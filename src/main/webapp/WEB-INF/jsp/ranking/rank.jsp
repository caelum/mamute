<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="menu.questions" var="title"/>
<c:url value="/rss" var="rssUrl" />

<ul class="ranking">
<c:forEach items="${topUsers}" var="user">
	<li><tags:RankingUser user="${user}"/></li>
</c:forEach>
</ul>
<tags:pagination url="${linkTo[RankingController].rank}" currentPage="${currentPage}" totalPages="${pages}" delta="2"/>

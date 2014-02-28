<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	<fmt:message key="users.ranking"/>
</h2>

<ul class="ranking">
<c:forEach items="${topUsers}" var="user">
	<li class="ranking-item"><tags:RankingUser user="${user}"/></li>
</c:forEach>
</ul>
<tags:pagination url="${linkTo[RankingController].rank}" currentPage="${currentPage}" totalPages="${pages}" delta="2"/>

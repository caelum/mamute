<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.home.title']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	${t['users.ranking']}
</h2>

<ul class="ranking">
<c:forEach items="${topUsers}" var="user">
	<li class="ranking-item"><tags:RankingUser user="${user}"/></li>
</c:forEach>
</ul>
<tags:pagination url="${linkTo[RankingController].rank}" currentPage="${currentPage}" totalPages="${pages}" delta="2"/>

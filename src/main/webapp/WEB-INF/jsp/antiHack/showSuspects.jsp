<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.home.title']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	${t['suspects.show'].args(voteType)}
	<tags:jodaTime pattern="dd/MM/YYYY" time="${startDate}"/>
	e
	<tags:jodaTime pattern="dd/MM/YYYY" time="${endDate}"/>
</h2>

<ul class="suspects">
	<li class="title page-title">
		<span class="answer-author">${t['suspects.suspect']}</span>
		<span class="count">${t['suspects.vote_received'].args(voteType)}</span>
		<span class="vote-author">${t['suspects.vote_author'].args(voteType)}</span>
	</li>
<c:forEach items="${suspects}" var="suspect">
	<li>
		<span class="answer-author"><tags:userProfileLink user="${suspect.answerAuthor}"/></span>
		<span class="count">${suspect.massiveVoteCount }</span>
		<span class="vote-author"><tags:userProfileLink user="${suspect.voteAuthor}"/></span>
	</li>
</c:forEach>
</ul>
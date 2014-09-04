<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.home.title" var="title"/>

<fmt:message key="metas.default.description" var="description">
	<fmt:param value="${siteName}" />
</fmt:message>

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	<fmt:message key="suspects.show"><fmt:param value="${voteType}"/></fmt:message>
	<tags:jodaTime pattern="dd/MM/YYYY" time="${startDate}"/>
	e
	<tags:jodaTime pattern="dd/MM/YYYY" time="${endDate}"/>
</h2>

<ul class="suspects">
	<li class="title page-title">
		<span class="answer-author"><fmt:message key="suspects.suspect"/></span>
		<span class="count"><fmt:message key="suspects.vote_received"><fmt:param value="${voteType}"/></fmt:message></span>
		<span class="vote-author"><fmt:message key="suspects.vote_author"><fmt:param value="${voteType}"/></fmt:message></span>
	</li>
<c:forEach items="${suspects}" var="suspect">
	<li>
		<span class="answer-author"><tags:userProfileLink user="${suspect.answerAuthor}"/></span>
		<span class="count">${suspect.massiveVoteCount }</span>
		<span class="vote-author"><tags:userProfileLink user="${suspect.voteAuthor}"/></span>
	</li>
</c:forEach>
</ul>
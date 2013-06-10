<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	Suspeitos de ${voteType} entre <tags:jodaTime pattern="dd/MM/YYYY" time="${startDate}"/> e <tags:jodaTime pattern="dd/MM/YYYY" time="${endDate}"/>
</h2>

<ul class="suspects">
	<li class="title page-title">
		<span class="answer-author">Suspeito</span>
		<span class="count">${voteType}s recebidos</span>
		<span class="vote-author">Autor do ${voteType}</span>
	</li>
<c:forEach items="${suspects}" var="suspect">
	<li>
		<span class="answer-author"><tags:userProfileLink user="${suspect.answerAuthor}" isPrivate="false"/></span>
		<span class="count">${suspect.massiveVoteCount }</span>
		<span class="vote-author"><tags:userProfileLink user="${suspect.voteAuthor}" isPrivate="false"/></span>
	</li>
</c:forEach>
</ul>
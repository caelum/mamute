<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="menu.questions" var="title"/>
<c:url value="/rss" var="rssUrl" />
<tags:questionList recentTags="${recentTags}" 
	questions="${questions}" title="${title}" rssUrl="${rssUrl}"/>

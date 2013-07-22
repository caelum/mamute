<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="menu.tags" var="title" />
<c:url value="/rss/${tag.name}" var="rssUrl" />
<section class="first-content">
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${title}" rssUrl="${rssUrl}"/>
</section>
<tags:sideBar recentTags="${recentTags}" />
<tags:joyrideIntro />
<tags:sideBar recentTags="${recentTags}" />

<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="tag_list.title" var="title" />
<c:url value="/rss/${tag.name}" var="rssUrl" />
<section class="first-content content">
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${}" rssUrl="${rssUrl}" unansweredTagLinks="${unansweredTagLinks}"/>
</section>
<tags:sideBar recentTags="${recentTags}" />
<tags:joyrideIntro />

<fmt:message key="metas.unsolved.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="menu.unsolved" var="title"/>
<section class="first-content content">
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${title}" rssUrl="${rssUrl}"/>
</section>
<tags:sideBar recentTags="${recentTags}" />
<tags:joyrideIntro />

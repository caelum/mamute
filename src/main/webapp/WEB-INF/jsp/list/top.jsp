<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<%-- <tags:tabs titleKey="menu.top">
	<a href="${linkTo[ListController].top('voted')}"><fmt:message key="menu.top.voted"/></a>
	<a href="${linkTo[ListController].top('answered')}"><fmt:message key="menu.top.answered"/></a>
</tags:tabs> --%>
<fmt:message key="menu.questions" var="title"/>

<section class="first-content content">
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${title}" tabs="${tabs}"/>
</section>

<tags:sideBar recentTags="${recentTags}" />

<tags:joyrideIntro />
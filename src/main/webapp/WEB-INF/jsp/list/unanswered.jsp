<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.unanswered.title']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<c:set var="title" value="${t['menu.unanswered']}"/>
<section class="first-content content">
	<tags:questionList recentTags="${recentTags}" 
			questions="${questions}" title="${title}" unansweredTagLinks="${true}"/>
</section>
<tags:sideBar recentTags="${recentTags}" />
<tags:joyrideIntro />

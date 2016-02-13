<c:set var="title" value="${t['metas.tags.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title subheader">${t['menu.tags']}</h2>
<ol class="tags-list complete-tags">
	<c:forEach var="tag" items="${tags}">
		<li class="complete-tag">
			<tags:tag tag="${tag}"/> x ${tag.usageCount}
			<div class='tag-description'>${tag.description}</div>
		</li>
	</c:forEach>
</ol>

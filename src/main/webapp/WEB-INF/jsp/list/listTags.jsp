<fmt:message key="metas.tags.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title replace"></h2>
<ol class="tags-list complete-tags">
	<c:forEach var="tag" items="${tags}">
		<li class="complete-tag">
			<tags:tag tag="${tag}"/> x ${tag.usageCount}
			<div class='tag-description'>${tag.description}</div>
		</li>
	</c:forEach>
</ol>

<tags:header title="metas.tags.title" description="metas.default.description"/>

<h2 class="title page-title replace"></h2>
<ol class="tags-list complete-tags">
	<c:forEach var="tagUsage" items="${tagsUsage}">
		<li class="complete-tag">
			<tags:tag tag="${tagUsage.tag}"/> x ${tagUsage.usage}
			<div class='tag-description'>${tagUsage.tag.description}</div>
		</li>
	</c:forEach>
</ol>

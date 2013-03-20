<tags:header facebookMetas="${true}" title="metas.home.title" description="metas.default.description"/>

<section class="first-content">
	<div class="subheader">
		<h2 class="title page-title replace"></h2>
	</div>
	<ol class="question-list">
		<c:forEach var="question" items="${questions }">
			<tags:list-question-item question="${question}"/>
		</c:forEach>
	</ol>
</section>
<aside class="sidebar">
	<div class="subheader">
		<h3 class="title page-title"><fmt:message key="tags.recent"/></h3>
	</div>
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
</aside>

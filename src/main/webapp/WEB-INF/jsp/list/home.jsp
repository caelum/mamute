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
	<h3 class="title section-title"><fmt:message key="tags.recent"/></h3>
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
</aside>
